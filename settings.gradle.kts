pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}


dependencyResolutionManagement {

    val localProperties = java.util.Properties().apply {
        val localPropertiesFile = file("local.properties")
        if (localPropertiesFile.exists()) {
            load(java.io.FileInputStream(localPropertiesFile))
        }
    }

    val user = localProperties.getProperty("gpr.user")
    val token = localProperties.getProperty("gpr.key")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/51mans0n/lab2andr")
            credentials {
                username = user
                password = token
            }
        }
    }
}

rootProject.name = "ChatAppDemo"
include(":app")
include(":chatlibrary")
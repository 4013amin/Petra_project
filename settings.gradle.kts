pluginManagement {
    repositories {
        gradlePluginPortal()
        google() // 🟢 ریپازیتوری Google
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // 🟢 ریپازیتوری Google
        mavenCentral()
    }
}


rootProject.name = "Shop_App_project"
include(":app")

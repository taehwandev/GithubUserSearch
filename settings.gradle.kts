pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "GitHubUserSearch"

include(":app")
include(":core:network")
include(":core:design-system")
include(":core:data:github")
include(":core:domain:github")
include(":core:database:github")
include(":core:database:github-api")
include(":feature:github-search")
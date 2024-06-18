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
        maven {   url = uri( "https://jitpack.io") }
    }
}
// --- 略 ---


rootProject.name = "イヌリキ"
include(":app")

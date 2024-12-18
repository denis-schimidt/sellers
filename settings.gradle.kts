pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.20"
    }
}
rootProject.name = "sellers"

gradle.startParameter.isBuildCacheEnabled = true

buildCache {
    local {
        directory = file("../.gradle/caches/")
    }
}

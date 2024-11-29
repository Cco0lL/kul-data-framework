plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")

    repositories { mavenCentral() }

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.test { useJUnitPlatform() }
}
plugins {
    kotlin("jvm") version "2.0.21"
}

subprojects {

    apply(plugin = "kotlin")

    repositories { mavenCentral() }

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.test { useJUnitPlatform() }
}
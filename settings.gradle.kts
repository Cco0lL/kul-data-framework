plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kul-data-framework"
include("core")
include("serialization-gson")
include("samples")

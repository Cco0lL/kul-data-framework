group = "kul.dataframework"
version = "1.0-SNAPSHOT"

dependencies {
    api(libs.gson)
    compileOnly(project(":core"))
}

publishing {
    publications {
        create<MavenPublication>("serialization-gson") {
            groupId = "kul.dataframework"
            artifactId = "serialization-gson"
            version = "1.0"
            from(components["kotlin"])
        }
    }
}
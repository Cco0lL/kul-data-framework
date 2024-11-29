group = "kul.dataframework"
version = "1.0-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("core") {
            groupId = "kul.dataframework"
            artifactId = "core"
            version = "1.0-SNAPSHOT"
            from(components["kotlin"])
        }
    }

}
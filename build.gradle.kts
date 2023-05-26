plugins {
    kotlin("jvm") version "1.8.21"
    id("maven-publish")
}

group = "com.huhx.snowflake"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        register<MavenPublication>("java") {
            groupId = "com.github.huhx"
            artifactId = "snowflake"
            version = "1.0.0"

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}

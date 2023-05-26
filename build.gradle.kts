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
        create<MavenPublication>("library") {
            from(components["kotlin"])
        }
    }
}

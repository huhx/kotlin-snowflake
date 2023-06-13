plugins {
    kotlin("jvm") version "1.8.21"
}

group = "com.huhx.snowflake"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

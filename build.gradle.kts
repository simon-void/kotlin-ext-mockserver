plugins {
    kotlin("jvm") version "1.9.10"
}

group = "de.gmx.simonvoid"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mock-server:mockserver-netty-no-dependencies:5.14.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useTestNG()
}

kotlin {
    jvmToolchain(17)
}
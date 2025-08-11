plugins {
    kotlin("jvm") version "2.1.21"
}

group = "io.github.liamkim-daeyong"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
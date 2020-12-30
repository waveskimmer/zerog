plugins {
    kotlin("jvm") version "1.4.20"
    kotlin("kapt") version "1.4.20"
    `java-library`
    `maven-publish`
}

group = "io.beansnapper"
version = "0.0.1"

tasks.withType<AbstractArchiveTask> {
    setProperty("archiveBaseName", "snap-builder-kapt")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.2.0")
    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation("com.squareup:kotlinpoet-metadata:1.7.2")
    implementation("com.squareup:kotlinpoet-metadata-specs:1.7.2")
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation(project(":annotation"))

    testImplementation("io.kotest:kotest-runner-junit5:4.3.1")
    testImplementation("io.kotest:kotest-assertions-core:4.3.1")
    testImplementation("io.kotest:kotest-property:4.3.1")
}

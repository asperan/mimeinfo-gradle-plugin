/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.gitSemVer)
}

group = "io.github.asperan"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

gitSemVer {
    buildMetadataSeparator.set("-")
    maxVersionLength.set(20)
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(libs.mimeinfo.core)
    testImplementation(gradleTestKit())
    testImplementation(libs.bundles.kotest)
}

detekt {
    buildUponDefaultConfig = true
    config.from("detekt-config.yaml")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

gradlePlugin {
    // Define the plugin
    val mimeInfo by plugins.creating {
        id = "io.github.asperan.mimeinfo-gradle-plugin"
        implementationClass = "io.github.asperan.mimeinfo.MimeInfoPlugin"
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    group = "verification"
    description = "Run functional tests"
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

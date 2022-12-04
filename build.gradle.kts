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
    id("com.gradle.plugin-publish") version "1.0.0"
    signing
    `maven-publish`
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
    val mimeInfoGradlePlugin by plugins.creating {
        id = "${project.group}.${project.name}"
        displayName = "MimeInfo Gradle Plugin"
        description = "Plugin to declare and install MIME types."
        implementationClass = "io.github.asperan.mimeinfo.MimeInfoPlugin"
    }
}

pluginBundle { // These settings are set for the whole plugin bundle
    vcsUrl = "https://github.com/asperan/mimeinfo-gradle-plugin"
    tags = listOf("mime", "mimetype", "mimeinfo", "asperan")
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

val javadocJar by tasks.registering(Jar::class) {
    from(tasks.dokkaJavadoc.get().outputDirectory)
    archiveClassifier.set("javadoc")
}

val sourceJar by tasks.registering(Jar::class) {
    from(sourceSets.named("main").get().allSource)
    archiveClassifier.set("sources")
}

publishing {
    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val mavenCentralPwd: String? by project
            credentials {
                username = "asperan"
                password = mavenCentralPwd
            }
        }
        publications {
            val mimeInfoGradlePlugin by creating(MavenPublication::class) {
                from(components["java"])
                artifact(javadocJar)
                artifact(sourceJar)
                pom {
                    name.set("MimeInfo Gradle Plugin")
                    description.set("MimeInfo Gradle Plugin allow to create and install MIME types.")
                    url.set("https://github.com/asperan/mimeinfo-gradle-plugin")
                    licenses {
                        license {
                            name.set("MPL2.0")
                        }
                    }
                    developers {
                        developer {
                            name.set("Alex Speranza")
                        }
                    }
                    scm {
                        url.set("git@github.com:asperan/mimeinfo-gradle-plugin.git")
                        connection.set("git@github.com:asperan/mimeinfo-gradle-plugin.git")
                    }
                }
            }
            signing { sign(mimeInfoGradlePlugin) }
        }
    }
}

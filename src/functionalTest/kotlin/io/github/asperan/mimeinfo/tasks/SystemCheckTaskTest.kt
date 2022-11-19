/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.tasks

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText

class SystemCheckTaskTest : StringSpec({
    val tempFolder = createTempDirectory()

    fun getProjectDir() = tempFolder
    fun getBuildFile() = getProjectDir().resolve("build.gradle")

    "System check on Linux is passed" {
        getBuildFile().writeText("""
            plugins {
                id('io.github.asperan.mimeinfo-gradle-plugin')
            }
            if (System.getProperty("os.name") != "Linux") {
                System.setProperty("os.name", "Linux")
            }
            """.trimIndent())

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("checkSystemIsSupported")
        runner.withProjectDir(getProjectDir().toFile())
        val result = runner.build()

        // Verify the result
        result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
    }

    "System check on another system fails" {
        getBuildFile().writeText("""
            plugins {
                id('io.github.asperan.mimeinfo-gradle-plugin')
            }
            if (System.getProperty("os.name") == "Linux") {
                System.setProperty("os.name", "AbsolutelyNotLinux")
            }
            """.trimIndent())

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("checkSystemIsSupported")
        runner.withProjectDir(getProjectDir().toFile())
        shouldThrow<UnexpectedBuildFailure> {
            runner.build()
        }
    }
})

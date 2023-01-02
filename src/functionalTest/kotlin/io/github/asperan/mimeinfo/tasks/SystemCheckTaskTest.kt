/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.tasks

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import kotlin.io.path.writeText

class SystemCheckTaskTest : TaskFunctionalTest({
    val taskName = "checkSystemIsSupported"

    "System check on Linux is passed" {
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            if (System.getProperty("os.name") != "Linux") {
                System.setProperty("os.name", "Linux")
            }
            """.trimIndent()
        )

        val result = runTask(taskName)

        // Verify the result
        result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
    }

    "System check on another system fails" {
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            if (System.getProperty("os.name") == "Linux") {
                System.setProperty("os.name", "AbsolutelyNotLinux")
            }
            """.trimIndent()
        )

        shouldThrow<UnexpectedBuildFailure> {
            runTask(taskName)
        }
    }
})

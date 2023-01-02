/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.tasks

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.StringSpec
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import kotlin.io.path.createTempDirectory

/**
 * Base class for functional tests.
 *
 * It provides base utilities like project files initialization and a shortcut method to run a task.
 * The unchecked cast is suppressed as a FunctionalTaskTest is always a StringSpec by definition.
 */
@Suppress("UNCHECKED_CAST")
@Ignored
abstract class TaskFunctionalTest(body: TaskFunctionalTest.() -> Unit = {}) : StringSpec(body as StringSpec.() -> Unit) {
    val projectDir = createTempDirectory()
    val buildFile = projectDir.resolve("build.gradle.kts")
    val settingsFile = projectDir.resolve("settings.gradle.kts")

    /**
     *  Run the task with the given name.
     */
    fun runTask(taskName: String): BuildResult = GradleRunner.create()
        .apply {
            forwardOutput()
            withPluginClasspath()
            withArguments(taskName)
            withProjectDir(this@TaskFunctionalTest.projectDir.toFile())
        }.build()
}

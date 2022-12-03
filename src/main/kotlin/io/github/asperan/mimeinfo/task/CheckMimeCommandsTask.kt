/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.task

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.process.internal.ExecException
import java.io.OutputStream
import javax.inject.Inject

/**
 * Task which groups tasks for checking mime-related commands.
 */
open class CheckMimeCommandsTask : DefaultTask() {
    init {
        val checkXdgMimeTask = project.createCheckCommandTask("xdg-mime")
        val checkXdgIconTask = project.createCheckCommandTask("xdg-icon-resource")
        val checkUpdateMimeDatabase = project.createCheckCommandTask("update-mime-database")
        this.dependsOn(
            checkXdgMimeTask,
            checkXdgIconTask,
            checkUpdateMimeDatabase,
        )
    }

    /**
     * Task for check the existence of a command.
     *
     * @param commandName The command to check.
     * @throws ExecException When the command does not exist.
     */
    open class CheckCommandTask @Inject constructor(@Internal val commandName: String) : Exec() {
        init {
            commandLine = listOf(
                "/bin/bash",
                "-c",
                "command -v $commandName",
            )
            standardOutput = OutputStream.nullOutputStream()
            // The exit value is ignored to provide a more meaningful error message in case of failure.
            isIgnoreExitValue = true

            this.doLast {
                (it as CheckCommandTask).assertNormalExecution()
            }
        }

        private fun assertNormalExecution() = when (executionResult.get().exitValue) {
            0 -> Unit
            else -> throw ExecException("Task $name failed: command '$commandName' does not exist")
        }
    }

    private companion object {
        private fun getTaskNameFromCommand(command: String): String =
            "check${command.split("-").joinToString("") { it.capitalized() }}CommandTask"

        /**
         * Register a new task of type CheckCommandTask.
         *
         * @param commandName The command to be checked.
         */
        private fun Project.createCheckCommandTask(commandName: String): TaskProvider<CheckCommandTask> =
            tasks.register(getTaskNameFromCommand(commandName), CheckCommandTask::class.java, commandName)
    }
}

/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty

/**
 * This task checks if the system is supported.
 */
open class SystemCheckTask : DefaultTask() {
    /**
     * The list of the supported OSes.
     */
    private val supportedSystems: ListProperty<String> = project.objects.listProperty()

    init {
        supportedSystems.value(listOf("Linux"))
        group = "verification"
        description = "A task which checks if the current system uses the shared MIME database"
    }

    /**
     * Task action. Checks whether the current system is supported by the plugin.
     */
    @TaskAction
    fun isSystemSupported() {
        val currentSystem = System.getProperty("os.name")
        check(supportedSystems.get().contains(currentSystem)) {
            "The current system '$currentSystem' is not supported.\n" +
                "Supported systems:\n${supportedSystems.get().joinToString("\n") { "- $it" }}"
        }
    }
}

/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * A plugin to create and install MIME types in the Linux shared database.
 */
class MimeInfoPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // Register a task
        project.tasks.register("greeting") { task ->
            task.doLast {
                println("Hello from plugin 'io.github.asperan.mimeinfo-gradle-plugin'")
            }
        }
    }
}

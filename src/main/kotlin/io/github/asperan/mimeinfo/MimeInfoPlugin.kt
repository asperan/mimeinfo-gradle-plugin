/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo

import io.github.asperan.mimeinfo.extension.MimeInfoExtension
import io.github.asperan.mimeinfo.task.*
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.kotlin.dsl.register

/**
 * A plugin to create and install MIME types in the Linux shared database.
 */
open class MimeInfoPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val extension = extensions.create("mimeinfos", MimeInfoExtension::class.java)
            val systemCheckTask = tasks.register<SystemCheckTask>("checkSystemIsSupported")
            val checkMimeCommandsTask = tasks.register<CheckMimeCommandsTask>("checkMimeCommandsTask") {
                dependsOn(systemCheckTask)
            }
            val mimeInfoFileWriteTask = tasks.register<MimeInfoFileWriteTask>("writeMimeInfoFiles") {
                dependsOn(systemCheckTask)
            }
            val mimeInfoInstallTask = tasks.register<MimeInfoInstallTask>("installMimeInfos") {
                dependsOn(checkMimeCommandsTask, mimeInfoFileWriteTask)
            }
            val iconInstallTask = tasks.register<IconInstallTask>("installIcons") {
                dependsOn(mimeInfoInstallTask)
            }
            val mimeDatabaseUpdateTask = tasks.register<MimeDatabaseUpdateTask>("updateMimeDatabase") {
                dependsOn(mimeInfoInstallTask, iconInstallTask)
            }
            tasks.register<DefaultTask>("installMimeTypes") {
                dependsOn(mimeDatabaseUpdateTask)
            }
        }
    }
}

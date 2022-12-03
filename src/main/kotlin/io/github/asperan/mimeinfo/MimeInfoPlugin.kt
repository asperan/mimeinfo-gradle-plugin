/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo

import io.github.asperan.mimeinfo.extension.MimeInfoExtension
import io.github.asperan.mimeinfo.task.CheckMimeCommandsTask
import io.github.asperan.mimeinfo.task.IconInstallTask
import io.github.asperan.mimeinfo.task.MimeDatabaseUpdateTask
import io.github.asperan.mimeinfo.task.MimeInfoFileWriteTask
import io.github.asperan.mimeinfo.task.MimeInfoInstallTask
import io.github.asperan.mimeinfo.task.SystemCheckTask
import io.github.asperan.mimeinfo.utility.BUILDSCRIPT_CONFIGURATION_NAME
import io.github.asperan.mimeinfo.utility.EXTENSION_NAME
import io.github.asperan.mimeinfo.utility.MIMEINFO_DEPENDENCY
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.repositories

/**
 * A plugin to create and install MIME types in the Linux shared database.
 */
open class MimeInfoPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) = settings.gradle.rootProject {
        it.addDependencies()
        it.createExtension()
        it.registerPluginTasks()
    }

    private fun Project.addDependencies() = with(buildscript) {
        repositories {
            mavenCentral()
        }
        dependencies.add(BUILDSCRIPT_CONFIGURATION_NAME, MIMEINFO_DEPENDENCY)
    }

    private fun Project.createExtension() {
        extensions.create(EXTENSION_NAME, MimeInfoExtension::class.java)
    }

    private fun Project.registerPluginTasks() {
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
        // TODO: maybe installing icons can be performed before the installation of the mimetype.
        // TODO: study how to install icons in a sane way.
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

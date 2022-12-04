/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.task

import io.github.asperan.mimeinfo.extension.MimeInfoExtension
import io.github.asperan.mimeinfo.utility.EXTENSION_NAME
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.get
import javax.inject.Inject

/**
 * Task for installing a MIME info file.
 */
open class MimeInfoInstallTask : DefaultTask() {
    init {
        val mimeinfoFiles = (project.extensions[EXTENSION_NAME] as MimeInfoExtension).mimeInfoFiles.keys
        val indexes = 1..mimeinfoFiles.size
        val baseDependencies = listOf(this.dependsOn.toTypedArray())
        this.dependsOn(
            *mimeinfoFiles
                .zip(indexes)
                .map {
                    project.tasks.register(
                        "installSingleMimeinfo-${it.second}",
                        SingleInstallTask::class.java,
                        baseDependencies,
                        it.first.absolutePath,
                    )
                }
                .toTypedArray()
        )
    }

    /**
     * Task to install a single mime file.
     *
     * @param filePath The path of the file.
     */
    open class SingleInstallTask @Inject constructor(baseDependencies: List<Any>, filePath: String) : Exec() {
        init {
            this.dependsOn(*baseDependencies.toTypedArray())
            this.dependsOn(project.tasks.get("writeMimeInfoFiles"))
            commandLine = listOf(
                "/bin/bash", "-c",
                "xdg-mime install --novendor --mode user $filePath"
            )
        }
    }
}

/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.task

import io.github.asperan.helper.XML_HEADER
import io.github.asperan.mimeinfo.extension.MimeInfoExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get

/**
 * Task which prints the MimeInfo files.
 */
open class MimeInfoFileWriteTask : DefaultTask() {
    /**
     * Write the configured MimeInfoSpecs in files.
     */
    @TaskAction
    fun writeFiles() =
        (project.extensions["mimeinfos"] as MimeInfoExtension)
            .mimeInfoFiles
            .forEach { (file, mimeInfoSpecs) ->
                project
                    .projectDir
                    .resolve(file)
                    .apply { createNewFile() }
                    .writeText("${XML_HEADER}\n${mimeInfoSpecs.toXmlString(indentLevel = 0u)}")
            }
}

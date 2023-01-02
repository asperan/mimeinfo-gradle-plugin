/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.tasks

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.gradle.testkit.runner.TaskOutcome
import kotlin.io.path.readText
import kotlin.io.path.writeText

class MimeInfoInstallTaskTest : TaskFunctionalTest({
    val taskName = "installMimeInfos"
    "The task should install the mime type" {
        val myCustomType = "x-custom-text"
        val mimeFilePath = "./my-custom-text.xml"
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }    
            """.trimIndent()
        )

        buildFile.writeText(
            """
            import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
            mimeinfos {
                mimeinfo("$mimeFilePath") {
                    mimetype(MimeTypeSpecs.Type.MimeClass.TEXT, "$myCustomType") {
                        comment("My custom Text type")
                        glob("*.cstxt")
                        globDeleteAll = true
                    }
                }
            }
            """.trimIndent()
        )
        val testFile = projectDir.resolve("test.cstxt")
        testFile.toFile().writeText("test")
        val result = runTask(taskName)
        val outputFile = projectDir.resolve("output.txt")
        try {
            result.tasks.all { it.outcome == TaskOutcome.SUCCESS }.shouldBeTrue()
            withContext(Dispatchers.IO) {
                ProcessBuilder("/bin/bash", "-c", "xdg-mime query filetype ${testFile.toFile().absolutePath}")
                    .directory(projectDir.toFile())
                    .redirectOutput(outputFile.toFile())
                    .start()
                    .waitFor()
            }
            val output = outputFile.readText()
            output.trim() shouldBe "text/$myCustomType"
        } finally {
            withContext(Dispatchers.IO) {
                ProcessBuilder("/bin/bash", "-c", "xdg-mime uninstall $mimeFilePath")
                    .directory(projectDir.toFile())
                    .start()
                    .waitFor()
            }
        }
    }
})

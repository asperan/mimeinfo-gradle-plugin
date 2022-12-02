/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.tasks

import kotlin.io.path.writeText

class CheckMimeCommandsTaskTest : TaskFunctionalTest({
    "Test the xdg commands checking" {
        settingsFile.writeText(
            """
            plugins {
                id("io.github.asperan.mimeinfo-gradle-plugin")
            }
            """.trimIndent()
        )
        runTask("checkMimeCommands")
    }
})

/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.task

import org.gradle.api.tasks.Exec

/**
 * Task which updates the MIME database.
 *
 * This action seems to be not required: executing `xdg-mime install` already update the mime types.
 * If errors or problems occur, this class will be implemented.
 */
open class MimeDatabaseUpdateTask : Exec()

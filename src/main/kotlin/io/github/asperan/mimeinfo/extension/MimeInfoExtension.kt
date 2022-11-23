/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.extension

import io.github.asperan.mimeinfo.context.MimeInfoContext
import io.github.asperan.mimeinfo.mime.MimeInfoSpecs
import java.io.File

/**
 * MimeInfo plugin extension.
 */
open class MimeInfoExtension {
    /**
     * The MimeInfoSpecs with the given file name.
     */
    var mimeInfoFiles: Map<File, MimeInfoSpecs> private set

    init {
        mimeInfoFiles = mapOf()
    }

    /**
     * Define a new MimeInfo object in the plugin configuration.
     *
     * @param fileName The temporary file name where to save the MimeInfo specification.
     * @param configuration The configuration to be called on the MimeInfo context.
     */
    fun mimeinfo(fileName: String, configuration: MimeInfoContext.() -> Unit) = MimeInfoContext()
        .apply { configuration() }
        .let { mimeInfoFiles = mimeInfoFiles + (File(fileName) to it.build()) }
}

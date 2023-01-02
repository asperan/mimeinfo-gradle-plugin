/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.MimeInfoSpecs
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
import io.github.asperan.mimeinfo.utility.MimeElementContextMarker

/**
 *  Class used in the extension for the MimeInfo specification.
 */
@MimeElementContextMarker
class MimeInfoContext : Context<MimeInfoSpecs> {
    private val mimeInfoSpecsBuilder = MimeInfoSpecs.Builder()

    /**
     * Add a mimetype to the mimeinfo of the context.
     *
     * @param typeClass The class of the MimeType.
     * @param typeName The name of the type.
     * @param configuration The configuration of the mime type.
     */
    fun mimetype(
        typeClass: MimeTypeSpecs.Type.MimeClass,
        typeName: String,
        configuration: MimeTypeContext.() -> Unit,
    ): Unit = MimeTypeContext(typeClass, typeName)
        .apply { configuration() }
        .let { mimeInfoSpecsBuilder.addMimeType(it.build()) }

    override fun build(): MimeInfoSpecs = mimeInfoSpecsBuilder.build()
}

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.MimeTypeSpecs

/**
 * The context of MimeTypes.
 */
class MimeTypeContext : Context<MimeTypeSpecs> {
    private val mimeTypeSpecsBuilder = MimeTypeSpecs.Builder()

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

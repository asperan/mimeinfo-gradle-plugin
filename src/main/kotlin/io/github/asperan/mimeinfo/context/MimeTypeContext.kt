package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.MimeTypeSpecs

/**
 * The context of MimeTypes.
 */
class MimeTypeContext(typeClass: MimeTypeSpecs.Type.MimeClass, typeName: String) : Context<MimeTypeSpecs> {
    private val mimeTypeSpecsBuilder = MimeTypeSpecs.Builder()

    init {
        mimeTypeSpecsBuilder.setType(MimeTypeSpecs.Type(typeClass, typeName))
    }

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

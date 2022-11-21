package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.MimeTypeSpecs

/**
 * The context of MimeTypes.
 *
 * @param typeClass The class of the Mime type.
 * @param typeName The name of the type.
 */
class MimeTypeContext(typeClass: MimeTypeSpecs.Type.MimeClass, typeName: String) : Context<MimeTypeSpecs> {
    private val mimeTypeSpecsBuilder = MimeTypeSpecs.Builder()

    init {
        mimeTypeSpecsBuilder.setType(MimeTypeSpecs.Type(typeClass, typeName))
    }

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

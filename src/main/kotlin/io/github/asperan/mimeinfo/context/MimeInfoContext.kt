package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.MimeInfoSpecs
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs

/**
 *  Class used in the extension for the MimeInfo specification.
 */
class MimeInfoContext : Context<MimeInfoSpecs> {
    private val mimeInfoSpecsBuilder = MimeInfoSpecs.Builder()

    /**
     * Add a mimetype to the mimeinfo of the context.
     *
     * @param configuration The configuration of the mime type.
     */
    fun mimetype(typeClass: MimeTypeSpecs.Type.MimeClass, typeName: String, configuration: MimeTypeContext.() -> Unit) {
        val mimeTypeContext = MimeTypeContext(typeClass, typeName)
        mimeTypeContext.configuration()
        mimeInfoSpecsBuilder.addMimeType(mimeTypeContext.build())
    }

    override fun build(): MimeInfoSpecs = mimeInfoSpecsBuilder.build()
}

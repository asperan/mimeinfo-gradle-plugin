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
     * @param typeClass The class of the MimeType.
     * @param typeName The name of the type.
     * @param configuration The configuration of the mime type.
     */
    fun mimetype(
        typeClass: MimeTypeSpecs.Type.MimeClass,
        typeName: String,
        configuration: MimeTypeContext.() -> Unit,
    ): Unit = MimeTypeContext(typeClass, typeName)
            .also { it.configuration() }
            .let { mimeInfoSpecsBuilder.addMimeType(it.build()) }

    override fun build(): MimeInfoSpecs = mimeInfoSpecsBuilder.build()
}

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.Glob
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs

/**
 * The context of MimeTypes.
 *
 * @param typeClass The class of the Mime type.
 * @param typeName The name of the type.
 */
class MimeTypeContext(
    typeClass: MimeTypeSpecs.Type.MimeClass,
    typeName: String,
) : Context<MimeTypeSpecs> {
    private val mimeTypeSpecsBuilder = MimeTypeSpecs.Builder()

    init {
        mimeTypeSpecsBuilder.setType(MimeTypeSpecs.Type(typeClass, typeName))
    }

    /**
     * Whether to add the "glob delete all" element to the mime type.
     */
    var globDeleteAll: Boolean = false; set(value) = Unit.also { mimeTypeSpecsBuilder.setGlobDeleteAll(value) }

    /**
     * Add a glob object to the MimeType.
     *
     * @param pattern The pattern of the glob.
     * @param weight The weight of the glob. Defaults to 50, also if the weight is null.
     */
    fun glob(pattern: String, weight: UByte? = null): Unit =
        Unit.also { mimeTypeSpecsBuilder.addGlob(Glob(pattern, weight)) }

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

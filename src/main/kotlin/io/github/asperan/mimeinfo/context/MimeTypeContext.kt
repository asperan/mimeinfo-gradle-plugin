package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.Comment
import io.github.asperan.mimeinfo.mime.Glob
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
import io.github.asperan.mimeinfo.utility.asUnit

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
    var globDeleteAll: Boolean = false; set(value) = mimeTypeSpecsBuilder.setGlobDeleteAll(value).asUnit

    /**
     * Add a glob object to the MimeType.
     *
     * @param pattern The pattern of the glob.
     * @param weight The weight of the glob. Defaults to 50, also if the weight is null.
     */
    fun glob(pattern: String, weight: UByte? = null): Unit = mimeTypeSpecsBuilder.addGlob(Glob(pattern, weight)).asUnit

    /**
     * Add a comment element to the MimeType.
     *
     * @param value The comment.
     * @param xmlLang The xml language specification.
     */
    fun comment(value: String, xmlLang: String? = null) =
        mimeTypeSpecsBuilder.addComment(Comment(value, xmlLang)).asUnit

    /**
     * Whether to add the "magic delete all" to the mime type.
     */
    var magicDeleteAll: Boolean = false; set(value) = mimeTypeSpecsBuilder.setMagicDeleteAll(value).asUnit

    /**
     * Add a magic element to the MimeType.
     * @param configuration The configuration of the Magic element.
     */
    fun magic(configuration: MimeMagicContext.() -> Unit) = MimeMagicContext()
        .apply { configuration() }
        .let { mimeTypeSpecsBuilder.addMagic(it.build()) }

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

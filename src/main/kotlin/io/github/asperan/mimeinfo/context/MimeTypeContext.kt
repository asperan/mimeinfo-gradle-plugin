package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.Acronym
import io.github.asperan.mimeinfo.mime.Alias
import io.github.asperan.mimeinfo.mime.Comment
import io.github.asperan.mimeinfo.mime.Glob
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
import io.github.asperan.mimeinfo.mime.SubClassOf
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

    /**
     * Add an alias to the mimetype.
     *
     * @param type the type alias.
     */
    fun alias(type: String) = mimeTypeSpecsBuilder.addAlias(Alias(type)).asUnit

    /**
     * Add a super class to the mimetype.
     *
     * @param type The parent type.
     */
    fun subclassOf(type: String) = mimeTypeSpecsBuilder.addSuperClass(SubClassOf(type)).asUnit

    /**
     * Add an acronym to the mimetype.
     *
     * @param value The value of the acronym.
     * @param xmlLang The xml language specification.
     */
    fun acronym(value: String, xmlLang: String? = null) =
        mimeTypeSpecsBuilder.addAcronym(Acronym(value, xmlLang)).asUnit

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.Acronym
import io.github.asperan.mimeinfo.mime.Alias
import io.github.asperan.mimeinfo.mime.Comment
import io.github.asperan.mimeinfo.mime.ExpandedAcronym
import io.github.asperan.mimeinfo.mime.Glob
import io.github.asperan.mimeinfo.mime.Icon
import io.github.asperan.mimeinfo.mime.MimeTypeSpecs
import io.github.asperan.mimeinfo.mime.RootXml
import io.github.asperan.mimeinfo.mime.SubClassOf
import io.github.asperan.mimeinfo.utility.MimeElementContextMarker
import io.github.asperan.mimeinfo.utility.asUnit

/**
 * The context of MimeTypes.
 *
 * @param typeClass The class of the Mime type.
 * @param typeName The name of the type.
 */
@MimeElementContextMarker
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

    /**
     * Add an expanded acronym to the mimetype.
     *
     * @param value The value of the expanded acronym.
     * @param xmlLang The xml language specification.
     */
    fun expandedAcronym(value: String, xmlLang: String? = null) =
        mimeTypeSpecsBuilder.addExpandedAcronym(ExpandedAcronym(value, xmlLang)).asUnit

    /**
     * Set the icon for the mimetype.
     *
     * @param name The name of the icon.
     */
    fun icon(name: String) = mimeTypeSpecsBuilder.setIcon(Icon(name)).asUnit

    /**
     * Set the generic icon for the mimetype.
     *
     * @param name The name of the generic icon.
     */
    fun genericIcon(name: String) = mimeTypeSpecsBuilder.setGenericIcon(Icon(name)).asUnit

    /**
     * Set the root XML for the mimetype.
     *
     * @param namespaceUri The namespace URI of the root XML.
     * @param localName The localname of the root XML.
     */
    fun rootXml(namespaceUri: String, localName: String) =
        mimeTypeSpecsBuilder.setRootXml(RootXml(namespaceUri, localName)).asUnit

    /**
     * Add a treemagic to the mimetype.
     *
     * @param configuration The configuration of the tree magic.
     */
    fun treemagic(configuration: MimeTreeMagicContext.() -> Unit) = MimeTreeMagicContext()
        .apply { configuration() }
        .let { mimeTypeSpecsBuilder.addTreeMagic(it.build()) }

    override fun build(): MimeTypeSpecs = mimeTypeSpecsBuilder.build()
}

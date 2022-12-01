/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.TreeMatch
import io.github.asperan.mimeinfo.utility.MimeElementContextMarker

/**
 * The context for TreeMatch elements.
 */
@MimeElementContextMarker
class MimeTreeMatchContext(
    path: String,
) : Context<TreeMatch> {
    private val treeMatchBuilder = TreeMatch.Builder()

    init {
        treeMatchBuilder.setPath(path)
    }

    /**
     * Set the type of the treematch.
     */
    var type: TreeMatch.Type = TreeMatch.Type.FILE; set(value) {
        field = value
        treeMatchBuilder.setTreeMatchType(value)
    }

    /**
     * Set whether the treematch should match the case.
     */
    var matchCase: Boolean = false; set(value) {
        field = value
        treeMatchBuilder.setMatchCase(value)
    }

    /**
     * Set whether the treematch should check the executability of the path.
     */
    var isExecutable: Boolean = false; set(value) {
        field = value
        treeMatchBuilder.setIsExecutable(value)
    }

    /**
     * Set whether the path should not be empty.
     */
    var nonEmpty: Boolean = false; set(value) {
        field = value
        treeMatchBuilder.setNonEmpty(value)
    }

    /**
     * Set what mimetype for the file.
     */
    var mimetype: String = ""; set(value) {
        field = value
        treeMatchBuilder.setMimetype(value)
    }

    /*
    This method is disabled because the gradle fails with a StackOverflowError with no message.
    As long as the error is not resolved, the nesting of TreeMatches is not supported by the dsl.
    /**
     * Add a treematch child.
     *
     * @param path The path of the child.
     * @param configuration The configuration of the child.
     */
    fun treematch(path: String, configuration: MimeTreeMatchContext.() -> Unit): Unit = MimeTreeMatchContext(path)
        .apply { configuration() }
        .let { treeMatchBuilder.addTreeMatch(it.build()) }
    */
    override fun build(): TreeMatch = treeMatchBuilder.build()
}

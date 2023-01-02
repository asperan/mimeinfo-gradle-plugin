/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.TreeMagic
import io.github.asperan.mimeinfo.utility.MimeElementContextMarker

/**
 * The context for TreeMagic elements.
 */
@MimeElementContextMarker
class MimeTreeMagicContext : Context<TreeMagic> {
    private val treeMagicBuilder = TreeMagic.Builder()

    /**
     * Set the priority of the TreeMagic.
     */
    var priority: UByte = 50u; set(value) {
        field = value
        treeMagicBuilder.setPriority(value)
    }

    /**
     * Add a treematch child.
     *
     * @param path The path of the child.
     * @param configuration The configuration of the child.
     */
    fun treematch(path: String, configuration: MimeTreeMatchContext.() -> Unit): Unit = MimeTreeMatchContext(path)
        .apply { configuration() }
        .let { treeMagicBuilder.addTreeMatch(it.build()) }

    override fun build(): TreeMagic = treeMagicBuilder.build()
}

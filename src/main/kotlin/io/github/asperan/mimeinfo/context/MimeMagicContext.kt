/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.Magic
import io.github.asperan.mimeinfo.mime.Match
import io.github.asperan.mimeinfo.utility.MimeElementContextMarker
import io.github.asperan.mimeinfo.utility.asUnit

/**
 * The context of Magic elements.
 */
@MimeElementContextMarker
class MimeMagicContext : Context<Magic> {
    private val magicBuilder = Magic.Builder()

    /**
     * Add a match to the Magic element.
     *
     * @param type The type of the match.
     * @param offset The offset of the match.
     * @param value The value of the match.
     * @param mask The mask for the match.
     */
    fun match(type: Match.Type, offset: Match.Offset, value: String, mask: String? = null) =
        magicBuilder.addMatch(Match(type, offset, value, mask)).asUnit

    /**
     * The priority of the magic element.
     */
    var priority: UByte = 50u; set(value) = magicBuilder.setPriority(value).asUnit

    override fun build(): Magic = magicBuilder.build()
}

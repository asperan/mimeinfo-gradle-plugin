package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.Magic
import io.github.asperan.mimeinfo.mime.Match
import io.github.asperan.mimeinfo.utility.asUnit

/**
 * The context of Magic elements.
 */
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

package io.github.asperan.mimeinfo.context

import io.github.asperan.mimeinfo.mime.TreeMagic

/**
 * The context for TreeMagic elements.
 */
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

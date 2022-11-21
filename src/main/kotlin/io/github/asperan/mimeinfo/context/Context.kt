package io.github.asperan.mimeinfo.context

/**
 * Base interface for Contexts which simply defines that a Context can build an object.
 */
sealed interface Context<T> {
    /**
     * Build the object of the context.
     *
     * @return The object of the Context.
     */
    fun build(): T
}

/*
Copyright (c) 2022, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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

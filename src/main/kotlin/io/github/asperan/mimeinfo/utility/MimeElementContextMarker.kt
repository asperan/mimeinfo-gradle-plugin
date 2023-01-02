/*
Copyright (c) 2023, Alex Speranza

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.asperan.mimeinfo.utility

/**
 * Dsl marker for Contexts to prevent implicit scope leaking.
 */
@DslMarker
annotation class MimeElementContextMarker

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jetbrains.annotations.ApiStatus;

/// When this annotation is present on a method, it means calls to the method
/// must not be wrapped.
@MCPFriendly
@ApiStatus.Experimental // This isn't meant to be used to annotate outside Frappé, so don't.
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface CallerSensitive {
}

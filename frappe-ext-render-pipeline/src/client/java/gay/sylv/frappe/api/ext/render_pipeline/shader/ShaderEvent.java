/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline.shader;

import java.util.Collection;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface ShaderEvent extends ShaderItem {
	@Unmodifiable Collection<Parameter> parameters();

	Type result();

	@ApiStatus.NonExtendable
	interface Parameter {
		Type type();

		String name();
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline.shader;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ShaderGlobal extends ShaderItem {
	/// The data type of this global.
	Type type();
}

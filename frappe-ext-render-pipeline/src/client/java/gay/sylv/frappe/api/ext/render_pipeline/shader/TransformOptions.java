/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline.shader;

import gay.sylv.frappe.impl.ext.render_pipeline.shader.TransformOptionsImpl;

public interface TransformOptions {
	static TransformOptions of(boolean useUniformBlocks) {
		return new TransformOptionsImpl(useUniformBlocks);
	}

	boolean useUniformBlocks();
}

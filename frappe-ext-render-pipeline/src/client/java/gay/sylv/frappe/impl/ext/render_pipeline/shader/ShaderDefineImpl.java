/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.render_pipeline.shader;

import java.util.Collection;

import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderDefine;

public record ShaderDefineImpl(
		String identifier,
		String value,
		boolean pipelineSpecific,
		Collection<String> aliases
) implements ShaderDefine {
}

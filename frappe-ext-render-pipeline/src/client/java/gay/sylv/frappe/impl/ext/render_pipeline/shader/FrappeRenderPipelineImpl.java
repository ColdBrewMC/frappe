/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.render_pipeline.shader;

import java.util.HashMap;
import java.util.Map;

import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;

public record FrappeRenderPipelineImpl(
		ShaderFormat shaderFormat,
		Map<String, UniformType<?>> uniformTypes,
		Map<String, FrappeRenderPipeline.UniformGetter<?>> uniformGetters
) implements FrappeRenderPipeline {
	public static final Map<String, FrappeRenderPipeline> SHADER_FORMAT_ID_2_PIPELINE = new HashMap<>();

	@Override
	public <T> void defineUniform(
			String identifier,
			UniformType<T> uniformType,
			UniformGetter<T> uniformGetter
	) {
		this.uniformTypes.put(identifier, uniformType);
		this.uniformGetters.put(identifier, uniformGetter);
	}
}

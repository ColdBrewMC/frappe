/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline.shader;

import java.util.Set;

import org.jetbrains.annotations.ApiStatus;

/// A set of declared items (uniforms, globals, entrypoints, etc.) in shaders with specific data types.
///
/// This is useful for processing events and constants in a shader format.
@ApiStatus.NonExtendable
public interface PipelineStageFormat {
	String shaderFormatId();

	PipelineStage pipelineStage();

	Set<ShaderEvent> events();

	Set<ShaderGlobal> globals();

	Set<ShaderDefine> defines();
}

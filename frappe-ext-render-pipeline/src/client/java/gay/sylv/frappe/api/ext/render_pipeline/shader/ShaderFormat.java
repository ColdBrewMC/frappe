/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline.shader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionManager;
import gay.sylv.frappe.impl.ext.render_pipeline.shader.FrappeRenderPipelineMod;
import gay.sylv.frappe.impl.ext.render_pipeline.shader.ShaderFormatImpl;

/// A simple API for defining and implementing shader formats.
///
/// When the target shader is overridden, this API can be used to alias and transform
/// the shader in such a manner that it becomes compatible with the underlying
/// implementation (e.g. Sodium or Indigo).
@ApiStatus.NonExtendable
public interface ShaderFormat {
	static ShaderFormat getExtensionFormats(String id, Class<? extends RendererExtension> extensionClass) {
		for (ShaderFormat format : getExtensionFormats(extensionClass).values()) {
			if (format.id().contains(id)) {
				return format;
			}
		}

		throw new NullPointerException("No such shader format with ID '" + id + "'");
	}

	static @Unmodifiable Map<String, ShaderFormat> getExtensionFormats(String extensionId) {
		return Objects.requireNonNull(
				FrappeRenderPipelineMod.EXTENSION_ID_2_SHADER_FORMAT.get(extensionId),
				"No shader format for a Frappé extension with ID '" + extensionId + "' was found"
		);
	}

	static @Unmodifiable Map<String, ShaderFormat> getExtensionFormats(Class<? extends RendererExtension> extensionClass) {
		return getExtensionFormats(RendererExtensionManager.getExtensionId(extensionClass));
	}

	static ShaderFormat getFormatOrThrow(String formatId) {
		return Objects.requireNonNull(
				getFormat(formatId),
				"No shader format with ID '" + formatId + "' was found"
		);
	}

	static @Nullable ShaderFormat getFormat(String formatId) {
		return FrappeRenderPipelineMod.ID_2_SHADER_FORMAT.get(formatId);
	}

	/// A union of formats is most useful for shader transformation.
	///
	/// @return a combined [ShaderFormat].
	static ShaderFormat union(ShaderFormat... formats) {
		Map<PipelineStage, List<PipelineStageFormat>> stageFormats = new HashMap<>();
		Set<String> ids = new HashSet<>();

		for (ShaderFormat format : formats) {
			ids.addAll(format.id());

			for (PipelineStage stage : PipelineStage.values()) {
				stageFormats.computeIfAbsent(stage, _ -> new ArrayList<>())
						.addAll(format.getStageFormats(stage));
			}
		}

		return new ShaderFormatImpl(Map.copyOf(stageFormats), Set.copyOf(ids), null);
	}

	String combineShaders(PipelineStage pipelineStage, String mainShaderSource, Map<String, String> shaderSources);

	String transformSingleShader(PipelineStage pipelineStage, String shaderSource, String id, Map<String, String> perPipelineDefines);

	String transformCombinedShader(PipelineStage pipelineStage, String shaderSource);

	default String transformAndCombineShaders(PipelineStage pipelineStage, String mainShaderSource, Map<String, String> shaderSources, Map<String, Map<String, String>> perPipelineDefines) {
		Map<String, String> transformedSources = new HashMap<>(shaderSources.size());

		for (Map.Entry<String, String> shaderSource : shaderSources.entrySet()) {
			transformedSources.put(
					shaderSource.getKey(),
					this.transformSingleShader(pipelineStage, shaderSource.getValue(), shaderSource.getKey(), perPipelineDefines.get(shaderSource.getKey()))
			);
		}

		return this.transformCombinedShader(pipelineStage, this.combineShaders(pipelineStage, mainShaderSource, transformedSources)).replace("#custom import", "#import");
	}

	/// @return this format's IDs.
	/// @see #singleId()
	Set<String> id();

	/// @return this format's single ID or `null` if this format is a union.
	@Nullable String singleId();

	@Unmodifiable Collection<PipelineStageFormat> getStageFormats(PipelineStage pipelineStage);
}

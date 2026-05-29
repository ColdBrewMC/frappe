/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import net.minecraft.client.renderer.state.GameRenderState;

import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;
import gay.sylv.frappe.impl.ext.render_pipeline.shader.FrappeRenderPipelineImpl;

/// An abstraction for defining compatible render pipeline modifications.
@ApiStatus.NonExtendable
public interface FrappeRenderPipeline {
	/// This method constructs a new [FrappeRenderPipeline] if it does not already exist.
	///
	/// @return the [FrappeRenderPipeline] associated with the [ShaderFormat].
	static FrappeRenderPipeline getOrCreate(ShaderFormat format) {
		if (format.singleId() == null) {
			throw new IllegalArgumentException("Expected a single ShaderFormat, but a ShaderFormat union was passed.");
		}

		return FrappeRenderPipelineImpl.SHADER_FORMAT_ID_2_PIPELINE.computeIfAbsent(
				Objects.requireNonNull(format.singleId()),
				_ -> new FrappeRenderPipelineImpl(format, new HashMap<>(), new HashMap<>())
		);
	}

	/// @return whether the [ShaderFormat] exists.
	static boolean exists(ShaderFormat format) {
		if (format.singleId() == null) {
			throw new IllegalArgumentException("Expected a single ShaderFormat, but a ShaderFormat union was passed.");
		}

		return FrappeRenderPipelineImpl.SHADER_FORMAT_ID_2_PIPELINE.containsKey(format.singleId());
	}

	/// @return all existing pipelines.
	static @Unmodifiable Collection<FrappeRenderPipeline> getAllPipelines() {
		return FrappeRenderPipelineImpl.SHADER_FORMAT_ID_2_PIPELINE.values();
	}

	/// Define a uniform's value with a [UniformGetter].
	///
	/// This method must be called in a [gay.sylv.frappe.api.base.extension.RendererReadyEntrypoint].
	<T> void defineUniform(String identifier, UniformType<T> uniformType, UniformGetter<T> uniformGetter);

	/// @return this pipeline's shader format.
	ShaderFormat shaderFormat();

	/// @return a map of shader global identifiers to this pipeline's uniform types.
	@Unmodifiable Map<String, UniformType<?>> uniformTypes();

	/// @return a map of shader global identifiers to this pipeline's uniform getters.
	@Unmodifiable Map<String, UniformGetter<?>> uniformGetters();

	/// Retrieves uniform globals' values.
	@FunctionalInterface
	interface UniformGetter<T> {
		/// @return the value of this uniform.
		T getValue(GameRenderState gameRenderState);
	}

	final class UniformType<T> {
		public static final UniformType<Float> FLOAT = new UniformType<>(Float.class);
		public static final UniformType<Vector2fc> VEC2 = new UniformType<>(Vector2fc.class);
		public static final UniformType<Vector3fc> VEC3 = new UniformType<>(Vector3fc.class);
		public static final UniformType<Vector4fc> VEC4 = new UniformType<>(Vector4fc.class);
		private final Class<T> clazz;

		private UniformType(Class<T> clazz) {
			this.clazz = clazz;
		}

		public Class<T> getUnderlyingClass() {
			return this.clazz;
		}
	}
}

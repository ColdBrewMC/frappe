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
import java.util.stream.Collectors;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import net.minecraft.client.renderer.state.GameRenderState;

import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;
import gay.sylv.frappe.api.ext.render_pipeline.value.Attribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.BlockAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.DataType;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;
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
				_ -> new FrappeRenderPipelineImpl(
						format,
						new HashMap<>(),
						new HashMap<>(),
						new HashMap<>(),
						new HashMap<>(),
						new HashMap<>(),
						new HashMap<>()
				)
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

	/// @deprecated Use [#defineUniform(String, DataType, UniformGetter)]
	@Deprecated(forRemoval = true)
	default <T> FrappeRenderPipeline defineUniform(String identifier, UniformType<T> uniformType, UniformGetter<T> uniformGetter) {
		return this.defineUniform(identifier, uniformType.dataType, uniformGetter);
	}

	/// Define a uniform's value with a [UniformGetter].
	///
	/// This method must be called in a [gay.sylv.frappe.api.base.extension.RendererReadyEntrypoint].
	<T> FrappeRenderPipeline defineUniform(String identifier, DataType<T> dataType, UniformGetter<T> uniformGetter);

	/// Declare a [vertex attribute][VertexAttribute].
	///
	/// This method must be called in a [gay.sylv.frappe.api.base.extension.RendererReadyEntrypoint].
	///
	/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
	@ApiStatus.Experimental
	<T> FrappeRenderPipeline declareVertexAttribute(VertexAttribute<T> vertexAttribute);

	/// Declare a [quad attribute][QuadAttribute].
	///
	/// This method must be called in a [gay.sylv.frappe.api.base.extension.RendererReadyEntrypoint].
	///
	/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
	@ApiStatus.Experimental
	<T> FrappeRenderPipeline declareQuadAttribute(QuadAttribute<T> quadAttribute);

	/// Declare a [block attribute][BlockAttribute].
	///
	/// This method must be called in a [gay.sylv.frappe.api.base.extension.RendererReadyEntrypoint].
	///
	/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
	@ApiStatus.Experimental
	<T> FrappeRenderPipeline declareBlockAttribute(BlockAttribute<T> blockAttribute);

	/// @return this pipeline's shader format.
	ShaderFormat shaderFormat();

	/// @deprecated Use [#uniformDataTypes()]
	@Deprecated(forRemoval = true)
	default @Unmodifiable Map<String, UniformType<?>> uniformTypes() {
		// slow as hell but also binary compatible
		return this.uniformDataTypes()
				.entrySet()
				.stream()
				.map(entry -> Map.entry(entry.getKey(), UniformType.of(entry.getValue())))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/// @return a map of shader global identifiers to this pipeline's uniform data types.
	@Unmodifiable Map<String, DataType<?>> uniformDataTypes();

	/// @return a map of shader global identifiers to this pipeline's uniform getters.
	@Unmodifiable Map<String, UniformGetter<?>> uniformGetters();

	/// @return a map of shader global identifiers to this pipeline's [attributes][Attribute].
	/// @see VertexAttribute
	/// @see QuadAttribute
	/// @see BlockAttribute
	@Unmodifiable Map<String, Attribute<?>> attributes();

	/// @return a map of shader global identifiers to this pipeline's [vertex attributes][VertexAttribute].
	@Unmodifiable Map<String, VertexAttribute<?>> vertexAttributes();

	/// @return a map of shader global identifiers to this pipeline's [quad attributes][QuadAttribute].
	@Unmodifiable Map<String, QuadAttribute<?>> quadAttributes();

	/// @return a map of shader global identifiers to this pipeline's [block attributes][BlockAttribute].
	@Unmodifiable Map<String, BlockAttribute<?>> blockAttributes();

	/// Retrieves uniform globals' values.
	@FunctionalInterface
	interface UniformGetter<T> {
		/// @return the value of this uniform.
		T getValue(GameRenderState gameRenderState);
	}

	/// @deprecated Use [DataType]
	@Deprecated(forRemoval = true)
	final class UniformType<T> extends DataType<T> {
		public static final UniformType<Float> FLOAT = new UniformType<>(DataType.FLOAT);
		public static final UniformType<Vector2fc> VEC2 = new UniformType<>(DataType.VEC2);
		public static final UniformType<Vector3fc> VEC3 = new UniformType<>(DataType.VEC3);
		public static final UniformType<Vector4fc> VEC4 = new UniformType<>(DataType.VEC4);
		private final DataType<T> dataType;

		private UniformType(DataType<T> dataType) {
			super(dataType.getUnderlyingClass());
			this.dataType = dataType;
		}

		@SuppressWarnings("unchecked")
		private static <T> UniformType<T> of(DataType<T> dataType) {
			if (dataType.equals(DataType.FLOAT)) {
				return (UniformType<T>) FLOAT;
			} else if (dataType.equals(DataType.VEC2)) {
				return (UniformType<T>) VEC2;
			} else if (dataType.equals(DataType.VEC3)) {
				return (UniformType<T>) VEC3;
			} else if (dataType.equals(DataType.VEC4)) {
				return (UniformType<T>) VEC4;
			} else {
				throw new IllegalArgumentException("DataType does not correspond to a valid UniformType.");
			}
		}
	}
}

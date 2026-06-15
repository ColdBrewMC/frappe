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

import gay.sylv.frappe.api.ext.render_pipeline.value.DataType;
import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;
import gay.sylv.frappe.api.ext.render_pipeline.value.Attribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.BlockAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;

public record FrappeRenderPipelineImpl(
		ShaderFormat shaderFormat,
		Map<String, DataType<?>> uniformDataTypes,
		Map<String, FrappeRenderPipeline.UniformGetter<?>> uniformGetters,
		Map<String, Attribute<?>> attributes,
		Map<String, VertexAttribute<?>> vertexAttributes,
		Map<String, QuadAttribute<?>> quadAttributes,
		Map<String, BlockAttribute<?>> blockAttributes
) implements FrappeRenderPipeline {
	public static final Map<String, FrappeRenderPipeline> SHADER_FORMAT_ID_2_PIPELINE = new HashMap<>();

	@Override
	public <T> FrappeRenderPipeline defineUniform(
			String identifier,
			DataType<T> uniformType,
			UniformGetter<T> uniformGetter
	) {
		this.uniformDataTypes.put(identifier, uniformType);
		this.uniformGetters.put(identifier, uniformGetter);
		return this;
	}

	@Override
	public <T> FrappeRenderPipeline declareVertexAttribute(VertexAttribute<T> vertexAttribute) {
		VertexAttribute<?> vertexAttribute1 = this.vertexAttributes.put(vertexAttribute.identifier(), vertexAttribute);
		Attribute<?> attribute = this.attributes.put(vertexAttribute.identifier(), vertexAttribute);

		if (vertexAttribute1 != null) {
			throw new IllegalStateException("Duplicate vertex attribute: " + vertexAttribute1.identifier());
		}

		if (attribute != null) {
			throw new IllegalStateException("Duplicate attributes of multiple types: " + attribute.identifier());
		}

		return this;
	}

	@Override
	public <T> FrappeRenderPipeline declareQuadAttribute(QuadAttribute<T> quadAttribute) {
		QuadAttribute<?> quadAttribute1 = this.quadAttributes.put(quadAttribute.identifier(), quadAttribute);
		Attribute<?> attribute = this.attributes.put(quadAttribute.identifier(), quadAttribute);

		if (quadAttribute1 != null) {
			throw new IllegalStateException("Duplicate quad attribute: " + quadAttribute1.identifier());
		}

		if (attribute != null) {
			throw new IllegalStateException("Duplicate attributes of multiple types:" + attribute.identifier());
		}

		return this;
	}

	@Override
	public <T> FrappeRenderPipeline declareBlockAttribute(BlockAttribute<T> blockAttribute) {
		BlockAttribute<?> blockAttribute1 = this.blockAttributes.put(blockAttribute.identifier(), blockAttribute);
		Attribute<?> attribute = this.attributes.put(blockAttribute.identifier(), blockAttribute);

		if (blockAttribute1 != null) {
			throw new IllegalStateException("Duplicate block attribute: " + blockAttribute1.identifier());
		}

		if (attribute != null) {
			throw new IllegalStateException("Duplicate attributes of multiple types:" + attribute.identifier());
		}

		return this;
	}
}

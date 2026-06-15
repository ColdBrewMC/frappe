/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import static gay.sylv.frappe.api.ext.render_pipeline.value.DataType.FLOAT;
import static gay.sylv.frappe.api.ext.render_pipeline.value.DataType.VEC2;
import static gay.sylv.frappe.api.ext.render_pipeline.value.DataType.VEC3;
import static gay.sylv.frappe.api.ext.render_pipeline.value.DataType.VEC4;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

import gay.sylv.frappe.api.ext.render_pipeline.value.DataType;

public class IndigoPipelineUniform<T> implements AutoCloseable {
	public static final Map<String, IndigoPipelineUniform<?>> INSTANCES = new HashMap<>();

	private final String identifier;
	private final DataType<T> type;
	private final int size;
	private final GpuBuffer buffer;

	public IndigoPipelineUniform(String identifier, DataType<T> type) {
		this.identifier = identifier;
		Std140SizeCalculator calculator = new Std140SizeCalculator();

		if (type.equals(FLOAT)) {
			calculator.putFloat();
		} else if (type.equals(VEC2)) {
			calculator.putVec2();
		} else if (type.equals(VEC3)) {
			calculator.putVec3();
		} else if (type.equals(VEC4)) {
			calculator.putVec4();
		}

		this.type = type;
		this.size = calculator.get();
		this.buffer = RenderSystem.getDevice().createBuffer(() -> identifier + " (Frappé Render Pipeline Uniform)", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, this.size);
	}

	public void update(T value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			Std140Builder builder = Std140Builder.onStack(stack, this.size);

			if (this.type.equals(FLOAT)) {
				builder.putFloat((Float) value);
			} else if (this.type.equals(VEC2)) {
				builder.putVec2((Vector2fc) value);
			} else if (this.type.equals(VEC3)) {
				builder.putVec3((Vector3fc) value);
			} else if (this.type.equals(VEC4)) {
				builder.putVec4((Vector4fc) value);
			}

			RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), builder.get());
		}
	}

	@Override
	public void close() {
		this.buffer.close();
	}

	public String getIdentifier() {
		return identifier;
	}

	public GpuBuffer getBuffer() {
		return buffer;
	}
}

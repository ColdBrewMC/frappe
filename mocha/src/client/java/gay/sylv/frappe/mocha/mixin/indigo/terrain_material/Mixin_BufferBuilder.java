/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.lwjgl.system.MemoryUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.ExtTerrainMaterial_BufferBuilder;
import gay.sylv.frappe.mocha.impl.indigo.vertex.format.MochaVertexFormats;

@Mixin(BufferBuilder.class)
public abstract class Mixin_BufferBuilder implements ExtTerrainMaterial_BufferBuilder {
	@Shadow
	protected abstract long beginElement(VertexFormatElement element);

	@Shadow
	private int elementsToFill;

	@WrapOperation(method = "endLastVertex", at = @At(
			value = "FIELD",
			target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;elementsToFill:I",
			opcode = Opcodes.GETFIELD
			))
	private int addRemainingCustomElements(BufferBuilder instance, Operation<Integer> original) {
		boolean onlyCustomRemaining = VertexFormatElement.elementsFromMask(this.elementsToFill)
				.map(element -> element.equals(MochaVertexFormats.UV3) || element.equals(MochaVertexFormats.SIMPLE_MATERIAL_INFO))
				.reduce(true, (a, b) -> a && b);

		// Set reasonable defaults
		if (onlyCustomRemaining) {
			this.frappe$setUv(0, 0);
			this.frappe$setMaterialId((byte) 0);
			return 0;
		}

		return original.call(instance);
	}

	@WrapOperation(method = "<init>", at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/VertexFormat;contains(Lcom/mojang/blaze3d/vertex/VertexFormatElement;)Z"
			))
	private boolean allowNonPosition(VertexFormat instance, VertexFormatElement element, Operation<Boolean> original) {
		if (instance.equals(MochaVertexFormats.SIMPLE_TERRAIN) || instance.equals(MochaVertexFormats.COMPLEX_TERRAIN)) {
			return true;
		} else {
			return original.call(instance, element);
		}
	}

	@Override
	public VertexConsumer frappe$setUv(float u, float v) {
		long pointer = this.beginElement(MochaVertexFormats.UV3);

		if (pointer != -1) {
			MemoryUtil.memPutFloat(pointer, u);
			MemoryUtil.memPutFloat(pointer + 4, v);
		}

		return (VertexConsumer) this;
	}

	@Override
	public VertexConsumer frappe$setMaterialId(byte id) {
		long pointer = this.beginElement(MochaVertexFormats.SIMPLE_MATERIAL_INFO);

		if (pointer != -1) {
			MemoryUtil.memPutByte(pointer, id);
		}

		return (VertexConsumer) this;
	}

	@Override
	public VertexConsumer frappe$setCenterOffset(byte x, byte y, byte z) {
		long pointer = this.beginElement(MochaVertexFormats.CENTER_OFFSET);

		if (pointer != -1) {
			MemoryUtil.memPutByte(pointer, x);
			MemoryUtil.memPutByte(pointer + 1, y);
			MemoryUtil.memPutByte(pointer + 2, z);
		}

		return (VertexConsumer) this;
	}

	@Override
	public boolean frappe$setAo(float ao) {
		long pointer = this.beginElement(MochaVertexFormats.AO);

		if (pointer != -1) {
			MemoryUtil.memPutByte(pointer, MochaVertexFormats.packAo(ao));
		}

		return pointer != 1;
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_AO;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_U_0;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_V_0;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.HEADER_MOCHA_BITS;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.terrain_material.QE_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.QV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

@SuppressWarnings("UnstableApiUsage")
@Mixin(QuadViewImpl.class)
public abstract class Mixin_QuadViewImpl implements QV_ExtTerrainMaterial {
	@Shadow
	protected int[] data;

	@Shadow
	protected int baseIndex;

	@Override
	public TerrainMaterial frappe$terrainMaterial() {
		return MochaIndigoEncodingFormat.terrainMaterial(this.data[this.baseIndex + HEADER_MOCHA_BITS]);
	}

	@Override
	public float frappe$u(int vertexIndex) {
		return Float.intBitsToFloat(this.data[this.baseIndex + HEADER_MOCHA_BITS + FRAPPE_U_0 + vertexIndex * 2]);
	}

	@Override
	public float frappe$v(int vertexIndex) {
		return Float.intBitsToFloat(this.data[this.baseIndex + HEADER_MOCHA_BITS + FRAPPE_V_0 + vertexIndex * 2]);
	}

	@Override
	public float frappe$ao(int vertexIndex) {
		return Float.intBitsToFloat(this.data[this.baseIndex + HEADER_MOCHA_BITS + FRAPPE_AO + vertexIndex]);
	}

	@WrapOperation(
			method = "buffer(ILcom/mojang/blaze3d/vertex/VertexConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;addVertex(FFFIFFIIFFF)V"
			)
	)
	private void encodeTerrainMaterial(
			VertexConsumer instance,
			float x,
			float y,
			float z,
			int color,
			float u,
			float v,
			int overlayCoords,
			int lightCoords,
			float nx,
			float ny,
			float nz,
			Operation<Void> original,
			@Local(name = "i") int i
	) {
		//noinspection DataFlowIssue // "quad outputs" always use MutableQuadViewImpls
		QE_ExtTerrainMaterial materialQuad = FrappeMutableQuadView.of((MutableQuadViewImpl) (Object) this)
				.as(QE_ExtTerrainMaterial.class);
		IndigoTerrainMaterialExtension.buffer(
				instance,
				x,
				y,
				z,
				color,
				u,
				v,
				overlayCoords,
				lightCoords,
				nx,
				ny,
				nz,
				i,
				materialQuad
		);
	}
}

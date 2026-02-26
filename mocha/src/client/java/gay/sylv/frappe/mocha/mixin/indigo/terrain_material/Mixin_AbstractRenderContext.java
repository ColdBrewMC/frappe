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
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractRenderContext;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadView;
import gay.sylv.frappe.api.ext.terrain_material.QV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

// We use UV1 to encode the terrain material in each vertex.
@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractRenderContext.class)
public abstract class Mixin_AbstractRenderContext {
	@WrapOperation(
			method = "bufferQuad(Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/MutableQuadViewImpl;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
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
			@Local(argsOnly = true) MutableQuadViewImpl quad
	) {
		TerrainMaterial material = FrappeQuadView.of(quad).as(QV_ExtTerrainMaterial.class).frappe$terrainMaterial();
		int i = MochaIndigoEncodingFormat.TERRAIN_MATERIAL_2_INDEX.get(material);
		instance.addVertex(x, y, z);
		instance.setColor(color);
		instance.setUv(u, v);
		instance.setUv1(i, i > 0 ? 0xCAFE : 0);
		instance.setLight(lightCoords);
		instance.setNormal(nx, ny, nz);
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_U_0;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_V_0;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.HEADER_MOCHA_BITS;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.frappe.api.ext.terrain_material.QE_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterial;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MutableQuadViewImpl.class)
public abstract class Mixin_MutableQuadViewImpl extends QuadViewImpl implements QE_ExtTerrainMaterial {
	@Shadow
	public abstract MutableQuadViewImpl shadow$chunkLayer(ChunkSectionLayer layer);

	@Override
	public QE_ExtTerrainMaterial frappe$terrainMaterial(TerrainMaterial material) {
		if (material.complexity().equals(TerrainMaterial.Complexity.COMPLEX)) {
			if (this.chunkLayer().equals(ChunkSectionLayer.SOLID)) {
				this.shadow$chunkLayer(MOCHA_SOLID);
			} else if (this.chunkLayer().equals(ChunkSectionLayer.CUTOUT)) {
				this.shadow$chunkLayer(MOCHA_CUTOUT);
			}
		} else if (material.complexity().equals(TerrainMaterial.Complexity.ISOLATE)) {
			this.shadow$chunkLayer(((IndigoTerrainMaterial) material).getChunkLayer());

			if (this.chunkLayer().equals(MOCHA_CUTOUT) || this.chunkLayer().equals(MOCHA_SOLID)) {
				throw new IllegalStateException("Isolate TerrainMaterial has default complex pipeline");
			}
		}

		this.data[this.baseIndex + HEADER_MOCHA_BITS] =
				MochaIndigoEncodingFormat.terrainMaterial(this.data[this.baseIndex + HEADER_MOCHA_BITS], material);
		return this;
	}

	@Override
	public QE_ExtTerrainMaterial frappe$uv(int vertexIndex, float u, float v) {
		this.data[this.baseIndex + HEADER_MOCHA_BITS + FRAPPE_U_0 + vertexIndex * 2] = Float.floatToIntBits(u);
		this.data[this.baseIndex + HEADER_MOCHA_BITS + FRAPPE_V_0 + vertexIndex * 2] = Float.floatToIntBits(v);
		return this;
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_U_0;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.FRAPPE_V_0;
import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.HEADER_MOCHA_BITS;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID;

import java.util.Objects;

import net.caffeinemc.mods.sodium.client.render.frapi.wrapper.MutableQuadViewWrapper;
import net.caffeinemc.mods.sodium.client.render.frapi.wrapper.QuadViewWrapper;
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.model.QuadViewImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

import gay.sylv.frappe.api.ext.material.Material;
import gay.sylv.frappe.api.ext.terrain_material.QE_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterial;

@Mixin(MutableQuadViewWrapper.class)
public abstract class Mixin_MutableQuadViewWrapper extends QuadViewWrapper implements QE_ExtTerrainMaterial {
	public Mixin_MutableQuadViewWrapper(QuadViewImpl quad) {
		super(quad);
	}

	// why the fuck do they still call it renderLayer
	@Shadow
	public abstract QuadEmitter shadow$chunkLayer(ChunkSectionLayer renderLayer);

	@Shadow
	private MutableQuadViewImpl mutableQuad;

	@Override
	public QE_ExtTerrainMaterial frappe$terrainMaterial(TerrainMaterial material) {
		if (material.complexity().equals(Material.Complexity.COMPLEX)) {
			if (Objects.equals(this.chunkLayer(), ChunkSectionLayer.SOLID)) {
				this.shadow$chunkLayer(MOCHA_SOLID);
			} else if (Objects.equals(this.chunkLayer(), ChunkSectionLayer.CUTOUT)) {
				this.shadow$chunkLayer(MOCHA_CUTOUT);
			}
		} else if (material.complexity().equals(Material.Complexity.ISOLATE)) {
			this.shadow$chunkLayer(((IndigoTerrainMaterial) material).getChunkLayer());

			if (Objects.equals(this.chunkLayer(), MOCHA_CUTOUT) || Objects.equals(this.chunkLayer(), MOCHA_SOLID)) {
				throw new IllegalStateException("Isolate TerrainMaterial has default complex pipeline");
			}
		}

		this.mutableQuad.data[this.mutableQuad.baseIndex + HEADER_MOCHA_BITS] =
				MochaIndigoEncodingFormat.terrainMaterial(this.mutableQuad.data[this.mutableQuad.baseIndex + HEADER_MOCHA_BITS], material);
		return this;
	}

	@Override
	public QE_ExtTerrainMaterial frappe$uv(int vertexIndex, float u, float v) {
		this.mutableQuad.data[this.mutableQuad.baseIndex + HEADER_MOCHA_BITS + FRAPPE_U_0 + vertexIndex * 2] = Float.floatToIntBits(u);
		this.mutableQuad.data[this.mutableQuad.baseIndex + HEADER_MOCHA_BITS + FRAPPE_V_0 + vertexIndex * 2] = Float.floatToIntBits(v);
		return this;
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.HEADER_MOCHA_BITS;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.frappe.api.ext.terrain_material.QV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(QuadViewImpl.class)
public abstract class Mixin_QuadViewImpl<Q extends QV_ExtTerrainMaterial<Q>> implements QV_ExtTerrainMaterial<Q> {
	@Shadow
	protected int[] data;

	@Shadow
	protected int baseIndex;

	@WrapMethod(method = "chunkLayer")
	private ChunkSectionLayer retVanillaLayers(Operation<ChunkSectionLayer> original) {
		ChunkSectionLayer layer = original.call();

		return layer;
	}

	@Override
	public @Nullable TerrainMaterial frappe$terrainMaterial() {
		return MochaIndigoEncodingFormat.terrainMaterial(this.data[this.baseIndex + HEADER_MOCHA_BITS]);
	}
}

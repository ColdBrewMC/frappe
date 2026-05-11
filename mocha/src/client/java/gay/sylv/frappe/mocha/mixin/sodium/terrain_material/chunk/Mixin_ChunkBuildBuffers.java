/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.BakedChunkModelBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.bsp_tree.UpdatedQuadsList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gay.sylv.frappe.mocha.impl.sodium.Ext_PackedMaterials;

@Mixin(ChunkBuildBuffers.class)
public abstract class Mixin_ChunkBuildBuffers {
	@Shadow
	@Final
	private Reference2ReferenceOpenHashMap<TerrainRenderPass, BakedChunkModelBuilder> builders;

	@WrapMethod(method = "createMesh")
	private BuiltSectionMeshParts putPackedMaterials(
			TerrainRenderPass pass,
			int visibleSlices,
			boolean forceUnassigned,
			boolean sliceReordering,
			Operation<BuiltSectionMeshParts> original
	) {
		BakedChunkModelBuilder builder = this.builders.get(pass);
		BuiltSectionMeshParts parts = original.call(pass, visibleSlices, forceUnassigned, sliceReordering);

		//noinspection ConstantValue // nullable
		if (parts == null) {
			return null;
		}

		NativeImage packedMaterials = ((Ext_PackedMaterials) builder).mocha$getPackedMaterials();

		if (packedMaterials != null) {
			((Ext_PackedMaterials) parts).mocha$setPackedMaterials(packedMaterials);
		}

		return parts;
	}

	@WrapMethod(method = "createModifiedTranslucentMesh")
	private BuiltSectionMeshParts putPackedMaterialsTranslucent(
			UpdatedQuadsList updatedQuads,
			Operation<BuiltSectionMeshParts> original
	) {
		BakedChunkModelBuilder builder = this.builders.get(DefaultTerrainRenderPasses.TRANSLUCENT);
		BuiltSectionMeshParts parts = original.call(updatedQuads);
		NativeImage packedMaterials = ((Ext_PackedMaterials) builder).mocha$getPackedMaterials();

		if (packedMaterials != null) {
			((Ext_PackedMaterials) parts).mocha$setPackedMaterials(packedMaterials);
		}

		return parts;
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.NativeImage;
import net.caffeinemc.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import net.caffeinemc.mods.sodium.client.render.chunk.data.SectionRenderDataStorage;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.sodium.Ext_DeviceResources;
import gay.sylv.frappe.mocha.impl.sodium.Ext_PackedMaterials;

@Mixin(RenderRegionManager.class)
public abstract class Mixin_RenderRegionManager {
	@Definition(id = "section", field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegionManager$PendingSectionMeshUpload;section:Lnet/caffeinemc/mods/sodium/client/render/chunk/RenderSection;")
	@Definition(id = "getSectionIndex", method = "Lnet/caffeinemc/mods/sodium/client/render/chunk/RenderSection;getSectionIndex()I")
	@Definition(id = "setVertexData", method = "Lnet/caffeinemc/mods/sodium/client/render/chunk/data/SectionRenderDataStorage;setVertexData(ILnet/caffeinemc/mods/sodium/client/gl/arena/GlBufferSegment;[I)V")
	@Definition(id = "storage", local = @Local(type = SectionRenderDataStorage.class, name = "storage"))
	@Definition(id = "meshData", field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegionManager$PendingSectionMeshUpload;meshData:Lnet/caffeinemc/mods/sodium/client/render/chunk/data/BuiltSectionMeshParts;")
	@Definition(id = "getVertexSegments", method = "Lnet/caffeinemc/mods/sodium/client/render/chunk/data/BuiltSectionMeshParts;getVertexSegments()[I")
	@Expression("storage.setVertexData(?.section.getSectionIndex(), ?, @(?.meshData).getVertexSegments())")
	@ModifyExpressionValue(method = "uploadResults(Lnet/caffeinemc/mods/sodium/client/gl/device/CommandList;Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion;Ljava/util/Collection;)V", at = @At("MIXINEXTRAS:EXPRESSION"))
	private BuiltSectionMeshParts onUploadChunkData(
			BuiltSectionMeshParts original,
			@Local(name = "resources") RenderRegion.DeviceResources resources
	) {
		//noinspection DataFlowIssue
		NativeImage packedMaterials = ((Ext_PackedMaterials) original).mocha$getPackedMaterials();

		if (packedMaterials != null) {
			((Ext_DeviceResources) resources).mocha$writeMeshMaterials(packedMaterials);
		}

		return original;
	}
}

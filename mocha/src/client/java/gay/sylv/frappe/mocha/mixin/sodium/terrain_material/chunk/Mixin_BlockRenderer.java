/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import java.util.Arrays;
import java.util.function.Predicate;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.NativeImage;
import net.caffeinemc.mods.sodium.client.render.chunk.LocalSectionIndex;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.services.PlatformModelEmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.sodium.Ext_PackedMaterials;
import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumMaterials;
import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumUtils;

@Mixin(BlockRenderer.class)
public abstract class Mixin_BlockRenderer {
	@SuppressWarnings("LocalMayUseName") // can't find it, no name
	@WrapOperation(method = "processQuad", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/material/DefaultMaterials;forChunkLayer(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayer;)Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/material/Material;"))
	private Material useMochaLayers(
			ChunkSectionLayer layer,
			Operation<Material> original,
			@Local(argsOnly = true) MutableQuadViewImpl quad
	) {
		if (quad.getRenderType() == null || quad.getRenderType().translucent()) {
			return original.call(layer);
		}

		Material material = MochaSodiumMaterials.MOCHA_MATERIALS.get(quad.getRenderType());

		if (material == null) {
			return original.call(layer);
		}

		return material;
	}

	@WrapOperation(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/services/PlatformModelEmitter;emitModel(Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;Ljava/util/function/Predicate;Lnet/caffeinemc/mods/sodium/client/render/model/MutableQuadViewImpl;Lnet/minecraft/util/RandomSource;Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/caffeinemc/mods/sodium/client/services/PlatformModelEmitter$Bufferer;)V"))
	private void setBlockPosForModelEmitter(
			PlatformModelEmitter instance,
			BlockStateModel blockStateModel,
			Predicate<Direction> directionPredicate,
			MutableQuadViewImpl quadView,
			RandomSource randomSource,
			BlockAndTintGetter blockAndTintGetter,
			BlockPos blockPos,
			BlockState blockState,
			PlatformModelEmitter.Bufferer bufferer,
			Operation<Void> original
	) {
		ScopedValue.where(MochaSodiumUtils.QUAD_EMITTER_BLOCK_POS, new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
				.run(() -> original.call(instance, blockStateModel, directionPredicate, quadView, randomSource, blockAndTintGetter, blockPos, blockState, bufferer));
	}

	@Definition(id = "builder", local = @Local(type = ChunkModelBuilder.class, name = "builder"))
	@Definition(id = "getVertexBuffer", method = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/buffers/ChunkModelBuilder;getVertexBuffer(Lnet/caffeinemc/mods/sodium/client/model/quad/properties/ModelQuadFacing;)Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/builder/ChunkMeshBufferBuilder;")
	@Definition(id = "vertexBuffer", local = @Local(type = ChunkMeshBufferBuilder.class, name = "vertexBuffer"))
	@Expression("vertexBuffer = builder.getVertexBuffer(?)")
	@Inject(method = "bufferQuad", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
	private void bufferMochaQuad(
			MutableQuadViewImpl quad,
			float[] brightnesses,
			Material material,
			CallbackInfo ci,
			@Local(name = "builder") ChunkModelBuilder builder
	) {
		NativeImage packedMaterials = ((Ext_PackedMaterials) builder).mocha$getPackedMaterials();

		int materialId = MochaIndigoEncodingFormat.terrainMaterialInt(quad.data[quad.baseIndex + MochaIndigoEncodingFormat.HEADER_MOCHA_BITS]);

		if (materialId != 0 && packedMaterials == null) {
			packedMaterials = new NativeImage(NativeImage.Format.LUMINANCE, 4096, 256, true);
			((Ext_PackedMaterials) builder).mocha$setPackedMaterials(packedMaterials);
		}

		if (materialId != 0) {
			BlockPos blockPos = MochaSodiumUtils.QUAD_EMITTER_BLOCK_POS.get();
			int x = blockPos.getX() & 0xF;
			int y = blockPos.getY() & 0xF;
			int z = blockPos.getZ() & 0xF;
			int blockId = (y << 4 | z) << 4 | x;
			int chunkX = blockPos.getX() >> 4;
			int chunkY = blockPos.getY() >> 4;
			int chunkZ = blockPos.getZ() >> 4;
			int regionX = chunkX & RenderRegion.REGION_WIDTH_M;
			int regionY = chunkY & RenderRegion.REGION_HEIGHT_M;
			int regionZ = chunkZ & RenderRegion.REGION_LENGTH_M;
			int chunkId = LocalSectionIndex.pack(regionX, regionY, regionZ);
			packedMaterials.setPixel(blockId, chunkId, materialId);
		}
	}
}

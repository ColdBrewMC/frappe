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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumMaterials;
import gay.sylv.frappe.mocha.impl.sodium.vertex.format.ComplexVertex;

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

	@WrapOperation(method = "<init>", at = @At(
			value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;uninitializedQuad()[Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;"
			))
	private ChunkVertexEncoder.Vertex[] useComplexVertices(Operation<ChunkVertexEncoder.Vertex[]> original) {
		return ComplexVertex.uninitializedQuad();
	}

	@Definition(id = "vertexBuffer", local = @Local(type = ChunkMeshBufferBuilder.class, name = "vertexBuffer"))
	@Definition(
			id = "push",
			method = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/builder/ChunkMeshBufferBuilder;push([Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;I)V"
	)
	@Definition(id = "materialBits", local = @Local(type = int.class, name = "materialBits"))
	@Expression("vertexBuffer.push(?, @(materialBits))")
	@ModifyExpressionValue(method = "bufferQuad", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private int bufferMochaQuad(
			int original,
			@Local(argsOnly = true, name = "quad") MutableQuadViewImpl quad,
			@Local(name = "builder") ChunkModelBuilder builder
	) {
		return original | (MochaIndigoEncodingFormat.terrainMaterialInt(quad.data[quad.baseIndex + MochaIndigoEncodingFormat.HEADER_MOCHA_BITS]) << 4);
	}

	@Definition(
			id = "light",
			field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;light:I"
	)
	@Definition(id = "quad", local = @Local(type = MutableQuadViewImpl.class, name = "quad", argsOnly = true))
	@Definition(
			id = "getLight",
			method = "Lnet/caffeinemc/mods/sodium/client/render/model/MutableQuadViewImpl;getLight(I)I"
	)
	@Definition(
			id = "out",
			local = @Local(
					type = ChunkVertexEncoder.Vertex.class,
					name = "out"
			)
	)
	@Expression("out.light = quad.getLight(?)")
	@Inject(method = "bufferQuad", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void setFrappeUv(
			MutableQuadViewImpl quad,
			float[] brightnesses,
			Material material,
			CallbackInfo ci,
			@Local(name = "out") ChunkVertexEncoder.Vertex out,
			@Local(name = "srcIndex") int srcIndex
	) {
		ComplexVertex complexVertex = (ComplexVertex) out;
		complexVertex.frappeU = Float.intBitsToFloat(quad.data[quad.baseIndex + MochaIndigoEncodingFormat.HEADER_MOCHA_BITS + MochaIndigoEncodingFormat.FRAPPE_U_0 + srcIndex * 2]);
		complexVertex.frappeV = Float.intBitsToFloat(quad.data[quad.baseIndex + MochaIndigoEncodingFormat.HEADER_MOCHA_BITS + MochaIndigoEncodingFormat.FRAPPE_V_0 + srcIndex * 2]);
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterial;

@Mixin(ChunkSectionsToRender.class)
public abstract class Mixin_ChunkSectionsToRender {
	@Shadow
	@Final
	private int maxIndicesRequired;

	@Shadow
	@Final
	private GpuTextureView textureView;

	@Shadow
	@Final
	private EnumMap<ChunkSectionLayer, Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>>> drawGroupsPerLayer;

	@Shadow
	@Final
	private GpuBufferSlice[] chunkSectionInfos;

	/**
	 * @author Sylv
	 * @reason We add our own passes, so it's more useful and less annoying to just overwrite this.
	 */
	@Overwrite
	public void renderGroup(ChunkSectionLayerGroup group, GpuSampler sampler) {
		RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
		GpuBuffer defaultIndexBuffer = this.maxIndicesRequired == 0 ? null : autoIndices.getBuffer(this.maxIndicesRequired);
		VertexFormat.IndexType defaultIndexType = this.maxIndicesRequired == 0 ? null : autoIndices.type();
		ChunkSectionLayer[] layers = group.layers();

		for (ChunkSectionLayer layer : layers) {
			IndigoTerrainMaterial material = (IndigoTerrainMaterial) layer.mocha$getAssociatedMaterial();

			if (material != null && material.preRenderPassState() != null) {
				material.preRenderPassState().run();
			}
		}

		Minecraft minecraft = Minecraft.getInstance();
		boolean wireframe = SharedConstants.DEBUG_HOTKEYS && minecraft.wireframe;
		RenderTarget renderTarget = group.outputTarget();

		try (RenderPass renderPass = RenderSystem.getDevice()
				.createCommandEncoder()
				.createRenderPass(
						() -> "Section layers for " + group.label(),
						Objects.requireNonNull(renderTarget.getColorTextureView()),
						OptionalInt.empty(),
						renderTarget.getDepthTextureView(),
						OptionalDouble.empty()
				)) {
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.bindTexture(
					"Sampler0",
					this.textureView,
					sampler
			);
			renderPass.bindTexture(
					"Sampler2",
					minecraft.gameRenderer.lightmap(),
					RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
			);

			for (ChunkSectionLayer layer : layers) {
				IndigoTerrainMaterial material = (IndigoTerrainMaterial) layer.mocha$getAssociatedMaterial();

				if (material != null) {
					if (material.renderPassSetup() != null) {
						material.renderPassSetup().accept(renderPass);
					}
				}

				renderPass.setPipeline(wireframe ? layer.mocha$getWireframePipeline() : layer.pipeline());
				Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>> drawGroup = this.drawGroupsPerLayer
						.get(layer);

				for (List<RenderPass.Draw<GpuBufferSlice[]>> draws : drawGroup.values()) {
					if (!draws.isEmpty()) {
						if (layer == ChunkSectionLayer.TRANSLUCENT) {
							draws = draws.reversed();
						}

						renderPass.drawMultipleIndexed(
								draws,
								defaultIndexBuffer,
								defaultIndexType,
								List.of("ChunkSection"),
								this.chunkSectionInfos
						);
					}
				}

				if (material != null) {
					if (material.renderPassCleanup() != null) {
						material.renderPassCleanup().accept(renderPass);
					}
				}
			}
		}

		for (ChunkSectionLayer layer : layers) {
			IndigoTerrainMaterial material = (IndigoTerrainMaterial) layer.mocha$getAssociatedMaterial();

			if (material != null && material.postRenderPassState() != null) {
				material.postRenderPassState().run();
			}
		}
	}
}

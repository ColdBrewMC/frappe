/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import java.lang.ref.WeakReference;
import java.util.OptionalDouble;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.caffeinemc.mods.sodium.client.gl.arena.staging.StagingBuffer;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlBufferStreamer;
import net.caffeinemc.mods.sodium.client.gl.device.CommandList;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.sylv.frappe.mocha.impl.sodium.Ext_DeviceResources;

@Mixin(RenderRegion.DeviceResources.class)
public abstract class Mixin_RenderRegion_DeviceResources implements Ext_DeviceResources {
	@SuppressWarnings("NotNullFieldNotInitialized") // in <init>
	@Unique
	private static GpuTexture emptyTextureMaterialInfo;
	@SuppressWarnings("NotNullFieldNotInitialized") // in <init>
	@Unique
	private static GpuTextureView emptyTextureViewMaterialInfo;
	@SuppressWarnings("NotNullFieldNotInitialized") // in <init>
	@Unique
	private GlBufferStreamer frappeMaterialInfo;
	@SuppressWarnings("NotNullFieldNotInitialized") // in <init>
	@Unique
	private GpuSampler samplerMaterialInfo;
	@SuppressWarnings("NotNullFieldNotInitialized") // in <init>
	@Unique
	private GpuTexture textureMaterialInfo;
	@SuppressWarnings("NotNullFieldNotInitialized") // in <init>
	@Unique
	private GpuTextureView textureViewMaterialInfo;
	@Unique
	private @Nullable WeakReference<GpuTextureView> textureViewMaterialInfoRef;
	@Unique
	private boolean writtenToTexture;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(
			CommandList commandList,
			StagingBuffer stagingBuffer,
			CallbackInfo ci
	) {
		this.frappeMaterialInfo = new GlBufferStreamer(commandList, 4096 * 256, Integer.BYTES);
		GpuDevice device = RenderSystem.getDevice();
		this.samplerMaterialInfo = device.createSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.NEAREST, FilterMode.NEAREST, 1, OptionalDouble.empty());
		this.textureMaterialInfo = device.createTexture("Frappé Material Info SSBO-at-home", GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_COPY_DST, TextureFormat.RED8, 4096, 256, 1, 1);
		this.textureViewMaterialInfo = device.createTextureView(this.textureMaterialInfo);

		//noinspection ConstantValue
		if (emptyTextureMaterialInfo == null || emptyTextureViewMaterialInfo == null) {
			emptyTextureMaterialInfo = device.createTexture("Frappé Material Info SSBO-at-home", GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_COPY_DST, TextureFormat.RED8, 4096, 256, 1, 1);
			emptyTextureViewMaterialInfo = device.createTextureView(emptyTextureMaterialInfo);
		}
	}

	@Override
	public void mocha$writeMeshMaterials(NativeImage packedMaterials) {
		GpuDevice device = RenderSystem.getDevice();
		CommandEncoder commandEncoder = device.createCommandEncoder();
		commandEncoder.writeToTexture(this.textureMaterialInfo, packedMaterials);
		this.writtenToTexture = true;
	}

	@Override
	public Ext_DeviceResources mocha$prepareMeshMaterials(CommandList commandList) {
		return this;
	}

	@Override
	public GpuTextureView mocha$getTextureMaterialInfo() {
		if (this.writtenToTexture) {
			return this.textureViewMaterialInfo;
		} else {
			return emptyTextureViewMaterialInfo;
		}
	}

	@Override
	public GpuSampler mocha$getSamplerMaterialInfo() {
		return this.samplerMaterialInfo;
	}

	@Inject(method = "delete", at = @At("RETURN"))
	private void onDelete(
			CommandList commandList,
			CallbackInfo ci
	) {
		this.frappeMaterialInfo.delete(commandList);
		this.samplerMaterialInfo.close();
		this.textureMaterialInfo.close();
		this.writtenToTexture = false;
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import net.caffeinemc.mods.sodium.client.gl.arena.staging.StagingBuffer;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlBuffer;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlBufferStreamer;
import net.caffeinemc.mods.sodium.client.gl.device.CommandList;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
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
	private GlBufferStreamer frappeMaterialInfo;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(
			CommandList commandList,
			StagingBuffer stagingBuffer,
			CallbackInfo ci
	) {
		// I'm not sure why it's multiplied by 4, but it is lol
		this.frappeMaterialInfo = new GlBufferStreamer(commandList, 4096 * 4, Integer.BYTES);
	}

	@Override
	public void mocha$writeMeshMaterials(int[] packedMaterials) {
		// TODO: add a new method to GlBufferStreamer if this isn't performant enough
		for (int i = 0; i < packedMaterials.length; i++) {
			int packed = packedMaterials[i];
			this.frappeMaterialInfo.writeData(i, packed);
		}
	}

	@Override
	public GlBuffer mocha$prepareMeshMaterials(CommandList commandList) {
		return this.frappeMaterialInfo.prepare(commandList);
	}

	@Inject(method = "delete", at = @At("RETURN"))
	private void onDelete(
			CommandList commandList,
			CallbackInfo ci
	) {
		this.frappeMaterialInfo.delete(commandList);
	}
}

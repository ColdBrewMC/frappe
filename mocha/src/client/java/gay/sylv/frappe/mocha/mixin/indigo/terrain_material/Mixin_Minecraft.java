/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.chunkSectionLayerGroupInit;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.chunkSectionLayerInit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;

@Mixin(Minecraft.class)
public abstract class Mixin_Minecraft {
	@Inject(method = "<init>", at = @At("CTOR_HEAD"))
	private void afterInit(GameConfig gameConfig, CallbackInfo ci) {
		// Ensure CSL and CSLG are loaded and then initialize them
		//noinspection ConstantValue
		if (ChunkSectionLayer.TRANSLUCENT.translucent() && ChunkSectionLayerGroup.OPAQUE.ordinal() > 0) {
			chunkSectionLayerInit.run();
			chunkSectionLayerGroupInit.run();
		}
	}
}

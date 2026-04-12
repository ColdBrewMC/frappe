/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumMaterials;

@Mixin(DefaultTerrainRenderPasses.class)
public abstract class Mixin_DefaultTerrainRenderPasses {
	@Shadow
	@Mutable
	@Final
	public static TerrainRenderPass[] ALL;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo ci) {
		ALL = ArrayUtils.addAll(ALL, MochaSodiumMaterials.RENDER_PASSES.toArray(TerrainRenderPass[]::new));
	}
}

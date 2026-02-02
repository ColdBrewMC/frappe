/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.mixin.client.ext.fabric_renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;

import gay.sylv.conduit.impl.ext.fabric_renderer.RendererRegistryEventsImpl;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(
			method = "<init>",
			at = @At("CTOR_HEAD")
	)
	private void onInit(GameConfig gameConfig, CallbackInfo ci) {
		RendererRegistryEventsImpl.AFTER_REGISTRY.invoker().afterRegistry();
	}
}

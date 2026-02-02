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

import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.conduit.api.ext.fabric_renderer.RendererReadyEntrypoint;

@Mixin(Minecraft.class)
public final class MinecraftMixin {
	private MinecraftMixin() {
	}

	@Inject(
			method = "<init>",
			at = @At("CTOR_HEAD")
	)
	private void onInit(GameConfig gameConfig, CallbackInfo ci) {
		FabricLoader.getInstance().invokeEntrypoints(
				"conduit:renderer_ready",
				RendererReadyEntrypoint.class,
				RendererReadyEntrypoint::onRendererReady
		);
	}
}

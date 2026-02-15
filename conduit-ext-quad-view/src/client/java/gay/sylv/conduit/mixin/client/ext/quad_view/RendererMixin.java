/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.mixin.client.ext.quad_view;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

import gay.sylv.conduit.api.ext.fabric_renderer.ConduitRenderer;
import gay.sylv.conduit.api.ext.fabric_renderer.FabricRendererExtension;
import gay.sylv.conduit.impl.base.extension.ExtensionRegistryImpl;
import gay.sylv.conduit.impl.ext.fabric_renderer.RendererRegistryEventsImpl;

@Mixin(Renderer.class)
public abstract class RendererMixin implements ConduitRenderer {
	@Unique
	private static @Nullable FabricRendererExtension extension;

	@Override
	public String conduit$id() {
		return getExtension().getRendererId();
	}

	@Inject(
			method = "register",
			at = @At("RETURN")
	)
	private static void onRegister(Renderer renderer, CallbackInfo ci) {
		RendererRegistryEventsImpl.BEFORE_REGISTRY.invoker().beforeRegistry();
		ExtensionRegistryImpl.loadReadyExtensions();
	}

	@Unique
	private static FabricRendererExtension getExtension() {
		if (extension == null) {
			extension = (FabricRendererExtension) ExtensionRegistryImpl.getExtensionOrThrow(ExtensionRegistryImpl.getId(FabricRendererExtension.class));
		}

		return extension;
	}
}

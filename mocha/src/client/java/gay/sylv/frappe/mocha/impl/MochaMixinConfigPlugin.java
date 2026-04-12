/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl;

import java.util.List;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

public class MochaMixinConfigPlugin implements IMixinConfigPlugin {
	private static final Set<String> INDIGO_DISABLED_MIXINS = Set.of(
	);
	private static @Nullable Boolean sodiumLoaded;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public @Nullable String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(
			String targetClassName,
			String mixinClassName
	) {
		if (sodiumLoaded == null) {
			sodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
		}

		if (!sodiumLoaded && mixinClassName.startsWith("gay.sylv.frappe.mocha.mixin.sodium")) {
			return false;
		}

		//noinspection RedundantIfStatement // This is more readable
		if (sodiumLoaded && INDIGO_DISABLED_MIXINS.contains(mixinClassName)) {
			return false;
		}

		return true;
	}

	@Override
	public void acceptTargets(
			Set<String> myTargets,
			Set<String> otherTargets
	) {
	}

	@Override
	public List<String> getMixins() {
		return List.of();
	}

	@Override
	public void preApply(
			String targetClassName,
			ClassNode targetClass,
			String mixinClassName,
			IMixinInfo mixinInfo
	) {
	}

	@Override
	public void postApply(
			String targetClassName,
			ClassNode targetClass,
			String mixinClassName,
			IMixinInfo mixinInfo
	) {
	}
}

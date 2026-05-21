/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;
import gay.sylv.frappe.mocha.impl.base.MochaExtensionPackage;

public class MochaMixinConfigPlugin implements IMixinConfigPlugin {
	private static final Set<String> INDIGO_DISABLED_MIXINS = Set.of(
	);
	private static @Nullable Boolean sodiumLoaded;
	private static final Map<String, String> PKG_2_ID = new HashMap<>();

	private static boolean isExtensionLoaded(String clazzName) {
		String[] split = clazzName
				.replace("gay.sylv.frappe.mocha.mixin.", "")
				.split("(sodium|indigo)");

		String pkgId = split[1].split("\\.")[1];
		String[] pkgElements = clazzName.split("\\.");

		// we're good because this is a general Mixin
		if (Character.isUpperCase(pkgId.codePointAt(0))) {
			return true;
		}

		// Get only the package name, not the class name
		StringBuilder pkgNameBuilder = new StringBuilder();

		pkgNameBuilder.append(pkgElements[0]);

		for (int i = 1; i < pkgElements.length - 1; i++) {
			pkgNameBuilder.append('.');
			pkgNameBuilder.append(pkgElements[i]);
		}

		String pkgName = pkgNameBuilder.toString();

		String id = PKG_2_ID.computeIfAbsent(pkgId, _ -> {
			try {
				Class<?> clazz = Class.forName((pkgName + ".package-info").replace("/", "."));
				Package pkg = clazz.getClassLoader().getDefinedPackage(pkgName);
				return pkg.getAnnotation(MochaExtensionPackage.class).value();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		});

		return RendererExtensionMetadata.isExtensionEnabled(id);
	}

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

		if (sodiumLoaded && INDIGO_DISABLED_MIXINS.contains(mixinClassName)) {
			return false;
		}

		//noinspection RedundantIfStatement // This is more readable
		if (!isExtensionLoaded(mixinClassName)) {
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

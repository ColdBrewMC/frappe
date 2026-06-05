/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import java.util.Objects;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.TerrainMaterialResourcePack;

@Mixin(BuiltInPackSource.class)
public abstract class Mixin_BuiltInPackSource {
	@Inject(method = "listBundledPacks", at = @At("RETURN"))
	private void addBuiltInPack(Consumer<Pack> packConsumer, CallbackInfo ci) {
		Pack pack = Pack.readMetaAndCreate(
				TerrainMaterialResourcePack.INSTANCE.location(), TerrainMaterialResourcePack.RESOURCES_SUPPLIER,
				PackType.CLIENT_RESOURCES, TerrainMaterialResourcePack.SELECTION_CONFIG
		);
		packConsumer.accept(Objects.requireNonNull(pack));
	}
}

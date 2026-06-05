/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackRepository;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.TerrainMaterialResourcePack;

@Mixin(PackRepository.class)
public abstract class Mixin_PackRepository {
	@WrapMethod(method = "openAllSelected")
	private List<PackResources> addMochaPack(Operation<List<PackResources>> original) {
		List<PackResources> packResources = new ArrayList<>(original.call());
		packResources.add(TerrainMaterialResourcePack.INSTANCE);
		return packResources;
	}
}

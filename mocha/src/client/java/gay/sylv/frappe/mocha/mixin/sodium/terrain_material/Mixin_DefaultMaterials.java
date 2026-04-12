/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.Ext_ChunkSectionLayer;
import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumMaterials;

@Mixin(DefaultMaterials.class)
public abstract class Mixin_DefaultMaterials {
	@WrapMethod(method = "forChunkLayer")
	private static Material fixMatchEnumExtended(
			ChunkSectionLayer layer,
			Operation<Material> original
	) {
		try {
			return original.call(layer);
		} catch (MatchException e) {
			//noinspection ConstantValue // It's lying to you, it is Mixin'd
			if (layer instanceof Ext_ChunkSectionLayer ext) {
				if (!ext.mocha$isFromMocha()) {
					throw e;
				}

				Material material = MochaSodiumMaterials.MOCHA_MATERIALS.get(layer);

				if (material == null) {
					throw e;
				}

				return material;
			}

			throw e;
		}
	}
}

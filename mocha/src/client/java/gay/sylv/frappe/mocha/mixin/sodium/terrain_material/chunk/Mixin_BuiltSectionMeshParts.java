/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import net.caffeinemc.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import gay.sylv.frappe.mocha.impl.sodium.Ext_PackedMaterials;

@Mixin(BuiltSectionMeshParts.class)
public abstract class Mixin_BuiltSectionMeshParts implements Ext_PackedMaterials {
	@Unique
	private int[] packedMaterials;

	@Override
	public void mocha$setPackedMaterials(int[] packedMaterials) {
		this.packedMaterials = packedMaterials;
	}

	@Override
	public int[] mocha$getPackedMaterials() {
		return this.packedMaterials;
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderTextureSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkShaderTextureSlot.class)
public enum Mixin_ChunkShaderTextureSlot {
	@SuppressWarnings("AddedEnumConstantsNamePattern") // MCDev bug
	MOCHA_MATERIAL_INFO
}

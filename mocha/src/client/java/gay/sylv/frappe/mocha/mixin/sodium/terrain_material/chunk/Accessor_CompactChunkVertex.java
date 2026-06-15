/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.CompactChunkVertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CompactChunkVertex.class)
public interface Accessor_CompactChunkVertex {
	@Invoker("packTexture")
	static int mocha$packTexture(int u, int v) {
		throw new IllegalStateException("Implemented via Mixin.");
	}

	@Invoker("encodeTexture")
	static int mocha$encodeTexture(float center, float x) {
		throw new IllegalStateException("Implemented via Mixin.");
	}
}

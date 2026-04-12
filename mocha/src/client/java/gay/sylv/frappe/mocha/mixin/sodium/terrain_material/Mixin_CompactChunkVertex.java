/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.CompactChunkVertex;
import org.spongepowered.asm.mixin.Mixin;

// fuck you, *uncompacts your vertex format*
// sorry iGPU users. works on my machine!
// jokes aside, it does work good enough on my shittier laptop, so we'll see.
// honestly i think we should just not change this but conditionally change if this or something else is used, and then not change it for simple contexts but do it for complex and *certainly* for isolate.
// hey why don't we do some vertex smuggling x3
// nah we're going with some block pulling as I'm calling it
@Mixin(CompactChunkVertex.class)
public abstract class Mixin_CompactChunkVertex {
}

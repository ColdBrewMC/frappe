/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium;

import net.minecraft.core.BlockPos;

public final class MochaSodiumUtils {
	public static final ScopedValue<BlockPos> QUAD_EMITTER_BLOCK_POS = ScopedValue.newInstance();

	private MochaSodiumUtils() {
	}
}

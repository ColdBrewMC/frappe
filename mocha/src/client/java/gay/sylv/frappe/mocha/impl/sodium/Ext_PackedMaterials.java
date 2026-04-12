/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium;

import org.jspecify.annotations.Nullable;

public interface Ext_PackedMaterials {
	default void mocha$setPackedMaterials(int[] packedMaterials) {
		throw new IllegalStateException("Implemented via Mixin.");
	}

	default int @Nullable [] mocha$getPackedMaterials() {
		throw new IllegalStateException("Implemented via Mixin.");
	}
}

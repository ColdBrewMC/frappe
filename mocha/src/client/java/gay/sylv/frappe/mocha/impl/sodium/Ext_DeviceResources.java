/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium;

import net.caffeinemc.mods.sodium.client.gl.buffer.GlBuffer;
import net.caffeinemc.mods.sodium.client.gl.device.CommandList;

public interface Ext_DeviceResources {
	default void mocha$writeMeshMaterials(int[] packedMaterials) {
		throw new IllegalStateException("Implemented via Mixin.");
	}

	default GlBuffer mocha$prepareMeshMaterials(CommandList commandList) {
		throw new IllegalStateException("Implemented via Mixin.");
	}
}

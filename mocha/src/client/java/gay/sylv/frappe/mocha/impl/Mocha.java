/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl;

import net.minecraft.resources.Identifier;

public final class Mocha {
	private Mocha() {
	}

	public static Identifier modId(String path) {
		return Identifier.fromNamespaceAndPath("mocha", path);
	}
}

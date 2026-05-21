/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.config;

import gay.sylv.frappe.api.base.extension.MCPFriendly;
import gay.sylv.frappe.impl.base.FrappeExtensionMetadataUtil;

/// Methods for retrieving Frappé config values.
@MCPFriendly
public final class FrappeConfigUtil {
	private FrappeConfigUtil() {
	}

	public static boolean getBooleanProperty(String key, boolean orElse) {
		return FrappeExtensionMetadataUtil.getBooleanProperty(key, orElse);
	}
}

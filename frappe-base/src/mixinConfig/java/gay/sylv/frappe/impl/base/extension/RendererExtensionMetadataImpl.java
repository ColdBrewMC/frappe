/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base.extension;

import java.util.Map;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import net.fabricmc.loader.api.Version;

import gay.sylv.frappe.api.base.extension.MCPFriendly;
import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;
import gay.sylv.frappe.api.base.extension.SupportTier;

@MCPFriendly
public record RendererExtensionMetadataImpl(
		String id,
		Version version,
		SupportTier supportTier,
		boolean enabled,
		String implType,
		@Nullable String name,
		@Nullable String description,
		@Unmodifiable Map<String, String> config
) implements RendererExtensionMetadata {
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

import gay.sylv.frappe.api.base.config.FrappeConfigUtil;
import gay.sylv.frappe.impl.base.FrappeExtensionMetadataUtil;

/// This interface holds metadata about a renderer extension.
@MCPFriendly
@ApiStatus.NonExtendable
public interface RendererExtensionMetadata {
	static @Unmodifiable Collection<RendererExtensionMetadata> fromModMetadata(ModMetadata metadata) {
		if (metadata.containsCustomValue("frappe")) {
			CustomValue frappe = metadata.getCustomValue("frappe");

			if (frappe.getType().equals(CustomValue.CvType.OBJECT)) {
				if (FrappeExtensionMetadataUtil.propertyExists(frappe.getAsObject(), "override", CustomValue.CvType.BOOLEAN) && frappe.getAsObject().get("override").getAsBoolean()) {
					return List.of();
				}

				return List.of(FrappeExtensionMetadataUtil.getExtension(
						metadata,
						frappe.getAsObject()
				));
			}

			List<RendererExtensionMetadata> extensionMetadata = new ArrayList<>();

			for (CustomValue extensionValue : frappe.getAsArray()) {
				RendererExtensionMetadata subExtension = FrappeExtensionMetadataUtil.getSubExtension(extensionValue.getAsObject(), metadata.getId());

				if (subExtension != null) {
					extensionMetadata.add(subExtension);
				}
			}

			return Collections.unmodifiableList(extensionMetadata);
		} else {
			return List.of();
		}
	}

	static @Nullable RendererExtensionMetadata get(String id) {
		return FrappeExtensionMetadataUtil.ID_2_METADATA.get(id);
	}

	/// This method is safe to call as early as Mixin config plugins.
	///
	/// @return whether an extension with the given ID is enabled and will be loaded.
	static boolean isExtensionEnabled(String id) {
		RendererExtensionMetadata metadata = RendererExtensionMetadata.get(id);

		if (metadata != null) {
			return FrappeConfigUtil.getBooleanProperty(id + ".enabled", metadata.enabled());
		} else {
			return false;
		}
	}

	String id();

	Version version();

	SupportTier supportTier();

	boolean enabled();

	String implType();

	@Nullable String name();

	@Nullable String description();

	/// Overrides of Frappé's configuration file.
	@Unmodifiable Map<String, String> config();

	default String getNameOrId() {
		String name = this.name();

		if (name == null || name.isEmpty()) {
			return this.id();
		}

		return name;
	}
}

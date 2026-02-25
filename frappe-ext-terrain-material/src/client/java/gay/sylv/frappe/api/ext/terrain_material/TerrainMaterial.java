/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.terrain_material;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.api.base.extension.RendererExtensionManager;

/// A modification to terrain's vertex and fragment shaders.
///
/// Each [TerrainMaterial] **must** be
/// [registered][TerrainMaterialExtension#registerMaterial(TerrainMaterial)] before use.
///
/// This property may be set using [MQV_ExtTerrainMaterial#frappe$terrainMaterial(TerrainMaterial)].
@ApiStatus.NonExtendable
public interface TerrainMaterial {
	Identifier shaderId();

	String label();

	final class Builder {
		private final Identifier shaderId;
		private @Nullable String label;

		public Builder(Identifier shaderId) {
			this.shaderId = shaderId;
		}

		public static Builder of(Identifier shaderId) {
			return new Builder(shaderId);
		}

		public Builder label(String label) {
			this.label = label;
			return this;
		}

		public TerrainMaterial build() {
			if (this.label == null) {
				this.label = shaderId.toString();
			}

			return RendererExtensionManager.getExtension(TerrainMaterialExtension.class)
					.createChunkLayer(shaderId, label);
		}
	}
}

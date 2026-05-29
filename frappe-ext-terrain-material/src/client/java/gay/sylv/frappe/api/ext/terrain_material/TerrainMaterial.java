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

import gay.sylv.frappe.api.ext.material.Material;

/// A [Material] for terrain.
///
/// Each [TerrainMaterial] **must** be
/// [registered][TerrainMaterialExtension#registerMaterial(TerrainMaterial)] before use.
///
/// This property may be set using [MQV_ExtTerrainMaterial#frappe$terrainMaterial(TerrainMaterial)].
///
/// @see Material
@ApiStatus.NonExtendable
public interface TerrainMaterial extends Material {
	final class Builder {
		private final Identifier shaderId;
		private @Nullable String label;
		private @Nullable Complexity complexity;

		public Builder(Identifier shaderId) {
			this.shaderId = shaderId;
		}

		public static Builder of(Identifier shaderId) {
			return new Builder(shaderId);
		}

		/// @see #label()
		public Builder label(String label) {
			this.label = label;
			return this;
		}

		/// A complexity rating provides useful optimization hints for the renderer.
		/// @see #complexity()
		public Builder complexity(Complexity complexity) {
			this.complexity = complexity;
			return this;
		}

		public TerrainMaterial build() {
			if (label == null) {
				label = shaderId.toString();
			}

			if (complexity == null) {
				throw new NullPointerException("Error while building TerrainMaterial: terrain materials require a complexity rating");
			}

			return TerrainMaterialExtension.get()
					.createChunkLayer(
							shaderId,
							label,
							complexity
					);
		}
	}
}

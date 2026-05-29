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

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionManager;

public interface TerrainMaterialExtension extends RendererExtension {
	/// @see RendererExtensionManager#getExtension(Class)
	static TerrainMaterialExtension get() {
		return RendererExtensionManager.getExtension(TerrainMaterialExtension.class);
	}

	/// Registers a [TerrainMaterial].
	///
	/// Note that some implementations **may** impose restrictions on how many materials may be
	/// implemented at once. For example, Mocha allows up to 255 [materials][TerrainMaterial].
	///
	/// This method **must** only be invoked in the [TerrainMaterialRegistryEntrypoint].
	static void registerMaterial(TerrainMaterial material) {
		get().registerMaterialImpl(material);
	}

	/// @return a new instance of [TerrainMaterial].
	/// @see TerrainMaterial.Builder#build()
	@ApiStatus.OverrideOnly
	TerrainMaterial createChunkLayer(
			Identifier shaderId,
			String label,
			TerrainMaterial.Complexity complexity
	);

	/// @see #registerMaterial(TerrainMaterial)
	@ApiStatus.OverrideOnly
	void registerMaterialImpl(TerrainMaterial material);
}

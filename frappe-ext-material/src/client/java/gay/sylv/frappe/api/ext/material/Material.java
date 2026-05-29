/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.material;

import net.minecraft.resources.Identifier;

/// A modification to a game object's shader pipeline,
/// applying only to specific quads.
///
/// A game object is any object rendered in the level
/// including but not limited to terrain, entities,
/// and block entities.
public interface Material {
	Identifier shaderId();

	/// A label used in debugging.
	String label();

	/// @see Complexity
	Complexity complexity();

	/// @see Complexity#SIMPLE
	default boolean simple() {
		return complexity().equals(Complexity.SIMPLE);
	}

	/// A measure of how independent and resource intensive a particular material is.
	///
	/// @see #SIMPLE
	/// @see #COMPLEX
	/// @see #ISOLATE
	enum Complexity {
		/// This material's shader does not branch and uses only simple operations that may be repeated
		/// across all relevant game objects.
		SIMPLE,
		/// This material's shader uses more complicated operations that may be repeated across all
		/// other relevant game objects with complex materials.
		COMPLEX,
		/// This material's shader uses branches, uses very complicated operations, or requires extra
		/// uniforms, all of which absolutely must not be repeated for other game objects. Most
		/// implementations will put this material on its own
		/// [chunk layer][net.minecraft.client.renderer.chunk.ChunkSectionLayer] or even its own
		/// [pass][net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup], making it isolate.
		///
		/// Up to 7 isolate materials are guaranteed to be supported in compliant implementations.
		/// Registering any more materials is implementation defined behavior.
		ISOLATE,
	}
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline.shader;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface ShaderItem {
	/// An identifier.
	String identifier();

	/// A collection of identifiers of aliases to this item.
	Collection<String> aliases();

	enum Qualifier {
		CONST,
		IN,
		OUT,
		FLAT;

		@Override
		public String toString() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}

	enum Specifier {
		VEC2,
		IVEC2,
		UVEC2,
		MAT2X2,
		VEC3,
		IVEC3,
		UVEC3,
		MAT3X3,
		VEC4,
		IVEC4,
		UVEC4,
		MAT4X4,
		FLOAT,
		INT,
		UINT,
		BOOL,
		SAMPLER2D,
		ISAMPLER2D,
		USAMPLER2D,
		ACCELERATION_STRUCTURE_EXT, // for when minecraft gets ray tracing in 2050
		VOID;

		@Override
		public String toString() {
			return this.name().toLowerCase(Locale.ROOT);
		}

		public boolean isOpaque() {
			return this.name().contains("SAMPLER");
		}
	}

	@ApiStatus.NonExtendable
	interface Type {
		boolean array();

		@Unmodifiable Set<Qualifier> qualifiers();

		Specifier specifier();
	}
}

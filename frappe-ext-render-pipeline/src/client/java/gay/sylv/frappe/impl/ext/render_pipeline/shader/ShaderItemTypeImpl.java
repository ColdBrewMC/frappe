/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.render_pipeline.shader;

import java.util.Arrays;
import java.util.Set;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderItem;

public record ShaderItemTypeImpl(
		boolean array,
		@Unmodifiable Set<ShaderItem.Qualifier> qualifiers,
		ShaderItem.Specifier specifier
) implements ShaderItem.Type {
	@NullMarked
	@Override
	public String toString() {
		String qualifiersString = Arrays.toString(qualifiers().toArray(new ShaderItem.Qualifier[qualifiers.size()]));
		qualifiersString = qualifiersString.substring(1, qualifiersString.length() - 1);
		qualifiersString = qualifiersString.replace(",", "");
		return (!qualifiersString.isEmpty() ? qualifiersString + " " : "") + specifier + (array ? "[]" : "");
	}
}

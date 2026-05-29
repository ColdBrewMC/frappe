/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.render_pipeline.shader;

import java.util.Collection;

import org.jspecify.annotations.NullMarked;

import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderEvent;

public record ShaderEventImpl(
		String identifier,
		Collection<Parameter> parameters,
		Type result,
		Collection<String> aliases
) implements ShaderEvent {
	@NullMarked
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
				.append(result)
				.append(" ")
				.append(identifier)
				.append("(");

		int i = 0;

		for (Parameter parameter : parameters) {
			if (i > 0) {
				builder.append(", ");
			}

			builder.append(parameter);
			i++;
		}

		return builder.append(");").toString();
	}
}

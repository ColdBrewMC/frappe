/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mixin.ext.render_pipeline;

import io.github.douira.glsl_transformer.ast.data.ChildNodeList;
import io.github.douira.glsl_transformer.ast.node.statement.ManyStatement;
import io.github.douira.glsl_transformer.ast.node.statement.Statement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ManyStatement.class)
public interface Accessor_ManyStatement {
	@Accessor("statements")
	void frappe_ext_render_pipeline$setStatements(ChildNodeList<Statement> statements);
}

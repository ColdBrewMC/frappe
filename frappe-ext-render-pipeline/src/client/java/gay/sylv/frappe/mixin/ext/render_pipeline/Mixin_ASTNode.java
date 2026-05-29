/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mixin.ext.render_pipeline;

import java.util.function.Consumer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.douira.glsl_transformer.ast.node.abstract_node.ASTNode;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ASTNode.class)
public abstract class Mixin_ASTNode {
	@WrapMethod(method = "updateParents")
	private <N extends ASTNode> void onlyUpdateNonNullParents(
			N currentNode,
			N newNode,
			Consumer<? extends N> setter,
			Operation<Void> original
	) {
		if (newNode == null) {
			return;
		}

		original.call(currentNode, newNode, setter);
	}
}

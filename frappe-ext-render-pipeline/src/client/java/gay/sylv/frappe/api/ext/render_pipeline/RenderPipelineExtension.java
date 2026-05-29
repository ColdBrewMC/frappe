/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.render_pipeline;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionManager;

/// Renderer-specific extensions for abstractions over vanilla render pipelines in conjunction with
/// a shader format transformer API.
///
/// Note that [gay.sylv.frappe.api.ext.render_pipeline.shader] requires this extension to be
/// implemented and loaded to work properly.
public interface RenderPipelineExtension extends RendererExtension {
	/// @see RendererExtensionManager#getExtension(Class)
	static RenderPipelineExtension get() {
		return RendererExtensionManager.getExtension(RenderPipelineExtension.class);
	}
}

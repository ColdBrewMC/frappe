package gay.sylv.frappe.impl.ext.quad_view;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionType;
import gay.sylv.frappe.api.base.extension.SupportTier;
import gay.sylv.frappe.api.ext.quad_view.QuadViewExtension;

public final class QuadViewExtensionType implements RendererExtensionType {
	@Override
	public SupportTier supportTier() {
		return SupportTier.CORE;
	}

	@Override
	public Class<? extends RendererExtension> implClass() {
		return QuadViewExtension.class;
	}
}

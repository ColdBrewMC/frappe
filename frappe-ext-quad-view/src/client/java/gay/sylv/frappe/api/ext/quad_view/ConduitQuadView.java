package gay.sylv.frappe.api.ext.quad_view;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;

@SuppressWarnings("unchecked")
public interface ConduitQuadView<Q extends QuadView> extends QuadView {
	static ConduitQuadView<QuadView> of(QuadView quad) {
		return (ConduitQuadView<QuadView>) quad;
	}

	@SuppressWarnings("rawtypes") // If we don't use a rawtype, IJ suddenly complains about unchecked types
	default <CQ extends ConduitQuadView> CQ as(Class<CQ> clazz) {
		return (CQ) this;
	}
}

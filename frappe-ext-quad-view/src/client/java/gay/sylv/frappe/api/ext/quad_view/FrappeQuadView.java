package gay.sylv.frappe.api.ext.quad_view;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;

@SuppressWarnings("unchecked")
public interface FrappeQuadView<Q extends QuadView> extends QuadView {
	static FrappeQuadView<QuadView> of(QuadView quad) {
		return (FrappeQuadView<QuadView>) quad;
	}

	@SuppressWarnings("rawtypes") // If we don't use a rawtype, IJ suddenly complains about unchecked types
	default <FQ extends FrappeQuadView> FQ as(Class<FQ> clazz) {
		return (FQ) this;
	}
}

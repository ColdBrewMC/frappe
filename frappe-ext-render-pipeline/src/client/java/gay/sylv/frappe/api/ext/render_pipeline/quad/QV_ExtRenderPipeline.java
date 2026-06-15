package gay.sylv.frappe.api.ext.render_pipeline.quad;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadView;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;

public interface QV_ExtRenderPipeline extends FrappeQuadView {
	/// The [vertex attribute][VertexAttribute] at a vertex or a default value like `0`.
	<T> T frappe$vertexAttribute(int vertexIndex, VertexAttribute<T> vertexAttribute);

	/// The [quad attribute][QuadAttribute] or a default value like `0`.
	<T> T frappe$quadAttribute(QuadAttribute<T> quadAttribute);
}

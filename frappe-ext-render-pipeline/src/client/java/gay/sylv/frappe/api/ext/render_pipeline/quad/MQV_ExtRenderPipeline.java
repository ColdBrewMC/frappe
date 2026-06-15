package gay.sylv.frappe.api.ext.render_pipeline.quad;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;

public interface MQV_ExtRenderPipeline extends FrappeMutableQuadView, QV_ExtRenderPipeline {
	/// Sets the specified [vertex attribute][VertexAttribute] at a vertex to the given value.
	<T> MQV_ExtRenderPipeline frappe$vertexAttribute(int vertexIndex, VertexAttribute<T> vertexAttribute, T value);

	/// Sets the specified [quad attribute][QuadAttribute] to the given value.
	<T> MQV_ExtRenderPipeline frappe$quadAttribute(QuadAttribute<T> quadAttribute, T value);
}

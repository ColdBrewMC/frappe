package gay.sylv.frappe.api.ext.render_pipeline.quad;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadEmitter;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;

public interface QE_ExtRenderPipeline extends FrappeQuadEmitter, MQV_ExtRenderPipeline {
	@Override
	<T> QE_ExtRenderPipeline frappe$vertexAttribute(
			int vertexIndex,
			VertexAttribute<T> vertexAttribute,
			T value
	);

	@Override
	<T> QE_ExtRenderPipeline frappe$quadAttribute(
			QuadAttribute<T> quadAttribute,
			T value
	);
}

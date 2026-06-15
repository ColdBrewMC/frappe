package gay.sylv.frappe.mocha.impl.indium.render_pipeline;

import java.util.Map;

import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;

public interface Ext_QuadData {
	default Map<VertexAttribute<?>, int[]> frappe$vertexAttributes() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default Map<QuadAttribute<?>, int[]> frappe$quadAttributes() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}
}

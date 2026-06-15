package gay.sylv.frappe.api.ext.render_pipeline.value;

import org.jetbrains.annotations.ApiStatus;

import gay.sylv.frappe.impl.ext.render_pipeline.shader.value.VertexAttributeImpl;

/// A custom vertex-specific value to be used in a material.
///
/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
@ApiStatus.Experimental
public interface VertexAttribute<T> extends Attribute<T> {
	static <T> VertexAttribute<T> of(String identifier, DataType<T> dataType) {
		return new VertexAttributeImpl<>(identifier, dataType);
	}
}

package gay.sylv.frappe.api.ext.render_pipeline.value;

import org.jetbrains.annotations.ApiStatus;

import gay.sylv.frappe.impl.ext.render_pipeline.shader.value.QuadAttributeImpl;

/// A custom quad-specific value to be used in a material.
///
/// This is functionally equivalent to a vertex attribute
/// with the same value across a quad.
///
/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
@ApiStatus.Experimental
public interface QuadAttribute<T> extends Attribute<T> {
	static <T> QuadAttribute<T> of(String identifier, DataType<T> dataType) {
		return new QuadAttributeImpl<>(identifier, dataType);
	}
}

package gay.sylv.frappe.api.ext.render_pipeline.value;

import org.jetbrains.annotations.ApiStatus;

import gay.sylv.frappe.impl.ext.render_pipeline.shader.value.BlockAttributeImpl;

/// A custom block-specific value to be used in a material.
///
/// This is functionally equivalent to a vertex attribute
/// with the same value across a block's quads.
///
/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
@ApiStatus.Experimental
public interface BlockAttribute<T> extends Attribute<T> {
	static <T> BlockAttribute<T> of(String identifier, DataType<T> dataType) {
		return new BlockAttributeImpl<>(identifier, dataType);
	}
}

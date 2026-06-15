package gay.sylv.frappe.impl.ext.render_pipeline.shader.value;

import gay.sylv.frappe.api.ext.render_pipeline.value.DataType;
import gay.sylv.frappe.api.ext.render_pipeline.value.BlockAttribute;

public record BlockAttributeImpl<T>(
		String identifier,
		DataType<T> dataType
) implements BlockAttribute<T> {
}

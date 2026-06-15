package gay.sylv.frappe.api.ext.render_pipeline.value;

/// A custom value to be used in a material.
///
/// **Warning:** Attributes are not required to be implemented yet and likely will not work.
public interface Attribute<T> {
	String identifier();

	DataType<T> dataType();
}

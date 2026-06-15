package gay.sylv.frappe.api.ext.render_pipeline.value;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

@ApiStatus.NonExtendable
public class DataType<T> {
	public static final DataType<Float> FLOAT = new DataType<>(Float.class);
	public static final DataType<Vector2fc> VEC2 = new DataType<>(Vector2fc.class);
	public static final DataType<Vector3fc> VEC3 = new DataType<>(Vector3fc.class);
	public static final DataType<Vector4fc> VEC4 = new DataType<>(Vector4fc.class);
	private final Class<T> clazz;

	protected DataType(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class<T> getUnderlyingClass() {
		return this.clazz;
	}
}

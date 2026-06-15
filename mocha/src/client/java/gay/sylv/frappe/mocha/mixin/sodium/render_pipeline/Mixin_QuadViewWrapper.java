package gay.sylv.frappe.mocha.mixin.sodium.render_pipeline;

import net.caffeinemc.mods.sodium.client.render.frapi.wrapper.QuadViewWrapper;
import net.caffeinemc.mods.sodium.client.render.model.QuadViewImpl;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import gay.sylv.frappe.api.ext.render_pipeline.quad.QV_ExtRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.value.DataType;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;
import gay.sylv.frappe.mocha.impl.indium.render_pipeline.Ext_QuadData;

@Mixin(QuadViewWrapper.class)
public abstract class Mixin_QuadViewWrapper implements QV_ExtRenderPipeline {
	@Unique
	private static final int[] EMPTY_QUAD = new int[4];
	@Unique
	private static final int[] EMPTY_VERT = new int[4 * 4];

	@Shadow
	private QuadViewImpl quad;

	@Override
	public <T> T frappe$vertexAttribute(
			int vertexIndex,
			VertexAttribute<T> vertexAttribute
	) {
		int[] data = quad2().frappe$vertexAttributes().get(vertexAttribute);

		if (data == null) {
			data = EMPTY_VERT;
		}

		if (vertexAttribute.dataType().equals(DataType.FLOAT)) {
			//noinspection unchecked
			return (T) Float.valueOf(Float.intBitsToFloat(data[vertexIndex]));
		} else if (vertexAttribute.dataType().equals(DataType.VEC2)) {
			float x = Float.intBitsToFloat(data[vertexIndex * 2]);
			float y = Float.intBitsToFloat(data[vertexIndex * 2 + 1]);
			//noinspection unchecked
			return (T) new Vector2f(x, y);
		} else if (vertexAttribute.dataType().equals(DataType.VEC3)) {
			float x = Float.intBitsToFloat(data[vertexIndex * 3]);
			float y = Float.intBitsToFloat(data[vertexIndex * 3 + 1]);
			float z = Float.intBitsToFloat(data[vertexIndex * 3 + 2]);
			//noinspection unchecked
			return (T) new Vector3f(x, y, z);
		} else if (vertexAttribute.dataType().equals(DataType.VEC4)) {
			float x = Float.intBitsToFloat(data[vertexIndex * 4]);
			float y = Float.intBitsToFloat(data[vertexIndex * 4 + 1]);
			float z = Float.intBitsToFloat(data[vertexIndex * 4 + 2]);
			float w = Float.intBitsToFloat(data[vertexIndex * 4 + 3]);
			//noinspection unchecked
			return (T) new Vector4f(x, y, z, w);
		} else {
			throw new UnsupportedOperationException("Unsupported DataType " + vertexAttribute.dataType() + " for vertex attribute " + vertexAttribute.identifier());
		}
	}

	@Override
	public <T> T frappe$quadAttribute(QuadAttribute<T> quadAttribute) {
		int[] data = quad2().frappe$quadAttributes().get(quadAttribute);

		if (data == null) {
			data = EMPTY_QUAD;
		}

		if (quadAttribute.dataType().equals(DataType.FLOAT)) {
			//noinspection unchecked
			return (T) Float.valueOf(Float.intBitsToFloat(data[0]));
		} else if (quadAttribute.dataType().equals(DataType.VEC2)) {
			float x = Float.intBitsToFloat(data[0]);
			float y = Float.intBitsToFloat(data[1]);
			//noinspection unchecked
			return (T) new Vector2f(x, y);
		} else if (quadAttribute.dataType().equals(DataType.VEC3)) {
			float x = Float.intBitsToFloat(data[0]);
			float y = Float.intBitsToFloat(data[1]);
			float z = Float.intBitsToFloat(data[2]);
			//noinspection unchecked
			return (T) new Vector3f(x, y, z);
		} else if (quadAttribute.dataType().equals(DataType.VEC4)) {
			float x = Float.intBitsToFloat(data[0]);
			float y = Float.intBitsToFloat(data[1]);
			float z = Float.intBitsToFloat(data[2]);
			float w = Float.intBitsToFloat(data[3]);
			//noinspection unchecked
			return (T) new Vector4f(x, y, z, w);
		} else {
			throw new UnsupportedOperationException("Unsupported DataType " + quadAttribute.dataType() + " for quad attribute " + quadAttribute.identifier());
		}
	}

	@Unique
	private Ext_QuadData quad2() {
		return (Ext_QuadData) this.quad;
	}
}

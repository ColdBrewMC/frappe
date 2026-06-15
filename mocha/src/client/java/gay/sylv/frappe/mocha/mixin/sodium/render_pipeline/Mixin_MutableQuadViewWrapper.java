package gay.sylv.frappe.mocha.mixin.sodium.render_pipeline;

import net.caffeinemc.mods.sodium.client.render.frapi.wrapper.MutableQuadViewWrapper;
import net.caffeinemc.mods.sodium.client.render.frapi.wrapper.QuadViewWrapper;
import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.model.QuadViewImpl;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import gay.sylv.frappe.api.ext.render_pipeline.quad.QE_ExtRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.value.DataType;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;
import gay.sylv.frappe.mocha.impl.indium.render_pipeline.Ext_QuadData;

@Mixin(MutableQuadViewWrapper.class)
public abstract class Mixin_MutableQuadViewWrapper extends QuadViewWrapper implements QE_ExtRenderPipeline {
	@Unique
	private static final int[] EMPTY_QUAD = new int[4];
	@Unique
	private static final int[] EMPTY_VERT = new int[4 * 4];

	@Shadow
	private MutableQuadViewImpl mutableQuad;

	public Mixin_MutableQuadViewWrapper(QuadViewImpl quad) {
		super(quad);
	}

	@Override
	public <T> QE_ExtRenderPipeline frappe$vertexAttribute(
			int vertexIndex,
			VertexAttribute<T> vertexAttribute,
			T value
	) {
		int[] data = quad2().frappe$vertexAttributes().get(vertexAttribute);

		if (data == null) {
			data = EMPTY_VERT;
		}

		if (vertexAttribute.dataType().equals(DataType.FLOAT)) {
			data[vertexIndex] = Float.floatToIntBits((Float) value);
		} else if (vertexAttribute.dataType().equals(DataType.VEC2)) {
			Vector2fc vector = (Vector2fc) value;
			data[vertexIndex * 2] = Float.floatToIntBits(vector.x());
			data[vertexIndex * 2 + 1] = Float.floatToIntBits(vector.y());
		} else if (vertexAttribute.dataType().equals(DataType.VEC3)) {
			Vector3fc vector = (Vector3fc) value;
			data[vertexIndex * 3] = Float.floatToIntBits(vector.x());
			data[vertexIndex * 3 + 1] = Float.floatToIntBits(vector.y());
			data[vertexIndex * 3 + 2] = Float.floatToIntBits(vector.z());
		} else if (vertexAttribute.dataType().equals(DataType.VEC4)) {
			Vector4fc vector = (Vector4fc) value;
			data[vertexIndex * 4] = Float.floatToIntBits(vector.x());
			data[vertexIndex * 4 + 1] = Float.floatToIntBits(vector.y());
			data[vertexIndex * 4 + 2] = Float.floatToIntBits(vector.z());
			data[vertexIndex * 4 + 3] = Float.floatToIntBits(vector.w());
		} else {
			throw new UnsupportedOperationException("Unsupported DataType " + vertexAttribute.dataType() + " for vertex attribute " + vertexAttribute.identifier());
		}

		return this;
	}

	@Override
	public <T> QE_ExtRenderPipeline frappe$quadAttribute(
			QuadAttribute<T> quadAttribute,
			T value
	) {
		int[] data = quad2().frappe$quadAttributes().get(quadAttribute);

		if (data == null) {
			data = EMPTY_QUAD;
		}

		if (quadAttribute.dataType().equals(DataType.FLOAT)) {
			data[0] = Float.floatToIntBits((Float) value);
		} else if (quadAttribute.dataType().equals(DataType.VEC2)) {
			Vector2fc vector = (Vector2fc) value;
			data[0] = Float.floatToIntBits(vector.x());
			data[1] = Float.floatToIntBits(vector.y());
		} else if (quadAttribute.dataType().equals(DataType.VEC3)) {
			Vector3fc vector = (Vector3fc) value;
			data[0] = Float.floatToIntBits(vector.x());
			data[1] = Float.floatToIntBits(vector.y());
			data[2] = Float.floatToIntBits(vector.z());
		} else if (quadAttribute.dataType().equals(DataType.VEC4)) {
			Vector4fc vector = (Vector4fc) value;
			data[0] = Float.floatToIntBits(vector.x());
			data[1] = Float.floatToIntBits(vector.y());
			data[2] = Float.floatToIntBits(vector.z());
			data[3] = Float.floatToIntBits(vector.w());
		} else {
			throw new UnsupportedOperationException("Unsupported DataType " + quadAttribute.dataType() + " for quad attribute " + quadAttribute.identifier());
		}

		return this;
	}

	@Unique
	private Ext_QuadData quad2() {
		return (Ext_QuadData) this.mutableQuad;
	}
}

package gay.sylv.conduit.impl.renderer.mesh;

import static gay.sylv.conduit.impl.renderer.mesh.PrimitiveHeaders.HEADER_STRIDE;
import static java.lang.Float.intBitsToFloat;

import java.util.NoSuchElementException;
import java.util.Set;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;

import gay.sylv.conduit.api.renderer.mesh.PrimitiveView;
import gay.sylv.conduit.api.renderer.mesh.VertexExtension;

public class PrimitiveViewImpl implements PrimitiveView {
	protected RenderPipeline pipeline;
	protected VertexFormat vertexFormat;
	protected int vertexStride;
	private int primitiveLength;
	protected Set<VertexExtension> vertexExtensions;
	protected int[] data;

	public PrimitiveViewImpl(RenderPipeline pipeline, int vertexExtensions) {
		this.initializePipeline(pipeline);
	}

	protected void initializePipeline(RenderPipeline pipeline) {
		this.pipeline = pipeline;
		this.vertexFormat = pipeline.getVertexFormat();
		this.vertexStride = this.vertexFormat.getVertexSize();
		this.primitiveLength = pipeline.getVertexFormatMode().primitiveLength;
		this.data = new int[HEADER_STRIDE + this.vertexStride];
		PrimitiveHeaders.PIPELINE.set(this.data, pipeline); // one pipeline per emitter
	}

	protected void assertExists(VertexFormatElement element) {
		if (!this.vertexFormat.contains(element)) {
			throw new NoSuchElementException("The element " + element + " does not exist on this primitive");
		}
	}

	protected boolean hasExtension(VertexExtension vertexExtension) {
		return this.vertexExtensions.contains(vertexExtension);
	}

	protected void assertExists(VertexExtension vertexExtension) {
		if (!this.hasExtension(vertexExtension)) {
			throw new NoSuchElementException(String.format("The vertex extension %s does not exist on this primitive", vertexExtension.name()));
		}
	}

	protected int getOffset(int vertexIndex, VertexFormatElement element) {
		this.assertExists(element);
		return this.getOffset(vertexIndex, this.vertexFormat.getOffset(element));
	}

	protected int getOffset(int vertexIndex, VertexExtension vertexExtension) {
		this.assertExists(vertexExtension);
		return this.getOffset(vertexIndex, vertexExtension.getVertexFormatElement());
	}

	protected int getOffset(int vertexIndex, int offset) {
		return HEADER_STRIDE + offset + vertexIndex * this.vertexStride;
	}

	protected int getData(int vertexIndex, VertexExtension vertexExtension) {
		return this.data[this.getOffset(vertexIndex, vertexExtension)];
	}

	protected int getData(int vertexIndex, VertexExtension vertexExtension, int offset) {
		return this.data[this.getOffset(vertexIndex, vertexExtension) + offset];
	}

	protected int getData(int vertexIndex, VertexFormatElement element) {
		return this.data[this.getOffset(vertexIndex, element)];
	}

	protected int getData(int vertexIndex, VertexFormatElement element, int offset) {
		return this.data[this.getOffset(vertexIndex, element) + offset];
	}

	protected int getData(int vertexIndex, int extOffset) {
		return this.data[this.getOffset(vertexIndex, extOffset)];
	}

	protected int getData(int vertexIndex, int extOffset, int offset) {
		return this.data[this.getOffset(vertexIndex, extOffset) + offset];
	}

	protected final Vector3fc computeSurfaceNormal() {
		// https://wikis.khronos.org/opengl/Calculating_a_Surface_Normal
		if (this.primitiveLength$conduit() == 3) {
		} else if (this.primitiveLength$conduit() == 4) {
		}
	}

	@Override
	public RenderPipeline pipeline$conduit() {
		return PrimitiveHeaders.PIPELINE.get(this.data);
	}

	@Override
	public TextureAtlas atlas$conduit() {
		return PrimitiveHeaders.ATLAS.get(this.data);
	}

	@Override
	public int primitiveLength$conduit() {
		return this.primitiveLength;
	}

	@Override
	public float x(int vertexIndex) {
		return this.posByIndex(vertexIndex, 0);
	}

	@Override
	public float y(int vertexIndex) {
		return this.posByIndex(vertexIndex, 1);
	}

	@Override
	public float z(int vertexIndex) {
		return this.posByIndex(vertexIndex, 2);
	}

	@Override
	public float posByIndex(int vertexIndex, int coordinateIndex) {
		return this.getData(vertexIndex, VertexFormatElement.POSITION, coordinateIndex);
	}

	@Override
	public Vector3f copyPos(int vertexIndex, @Nullable Vector3f target) {
		if (target == null) {
			return new Vector3f(this.x(vertexIndex), this.y(vertexIndex), this.z(vertexIndex));
		}

		target.x = this.x(vertexIndex);
		target.y = this.y(vertexIndex);
		target.z = this.z(vertexIndex);
		return target;
	}

	@Override
	public int color(int vertexIndex) {
		return this.getData(vertexIndex, VertexFormatElement.COLOR);
	}

	@Override
	public float u(int vertexIndex) {
		return intBitsToFloat(this.getData(vertexIndex, VertexFormatElement.UV));
	}

	@Override
	public float v(int vertexIndex) {
		return intBitsToFloat(this.getData(vertexIndex, VertexFormatElement.UV, 1));
	}

	@Override
	public Vector2f copyUv(int vertexIndex, @Nullable Vector2f target) {
		if (target == null) {
			return new Vector2f(this.u(vertexIndex), this.v(vertexIndex));
		}

		target.x = this.u(vertexIndex);
		target.y = this.v(vertexIndex);
		return target;
	}

	@Override
	public int lightmap(int vertexIndex) {
		return this.getData(vertexIndex, VertexExtension.LIGHTMAP);
	}

	@Override
	public boolean hasNormal(int vertexIndex) {
		return (PrimitiveHeaders.NORMAL_FLAGS.get(this.data) & (1 << vertexIndex)) != 0;
	}

	@Override
	public float normalX(int vertexIndex) {
		if (!this.hasNormal(vertexIndex)) return Float.NaN;

		if (!this.hasExtension(VertexExtension.NORMAL)) {
			return this.getData(vertexIndex, VertexFormatElement.NORMAL);
		}

		return this.getData(vertexIndex, VertexExtension.NORMAL);
	}

	@Override
	public float normalY(int vertexIndex) {
		if (!this.hasNormal(vertexIndex)) return Float.NaN;

		if (!this.hasExtension(VertexExtension.NORMAL)) {
			return this.getData(vertexIndex, VertexFormatElement.NORMAL, 1);
		}

		return this.getData(vertexIndex, VertexExtension.NORMAL, 1);
	}

	@Override
	public float normalZ(int vertexIndex) {
		if (!this.hasNormal(vertexIndex)) return Float.NaN;

		if (!this.hasExtension(VertexExtension.NORMAL)) {
			return this.getData(vertexIndex, VertexFormatElement.NORMAL, 1);
		}

		return this.getData(vertexIndex, VertexExtension.NORMAL, 1);
	}

	@Override
	public @Nullable Vector3f copyNormal(
			int vertexIndex,
			@Nullable Vector3f target
	) {
		if (!this.hasNormal(vertexIndex)) {
			return null;
		}

		if (target == null) {
			return new Vector3f(this.normalX(vertexIndex), this.normalY(vertexIndex), this.normalZ(vertexIndex));
		}

		target.x = this.normalX(vertexIndex);
		target.y = this.normalY(vertexIndex);
		target.z = this.normalZ(vertexIndex);
		return target;
	}

	@Override
	public Vector3fc faceNormal() {
		return null;
	}

	@Override
	public Direction lightFace() {
		return null;
	}
}

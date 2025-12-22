package gay.sylv.conduit.impl.renderer.mesh;

import static java.lang.Float.floatToIntBits;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import gay.sylv.conduit.api.renderer.mesh.MutablePrimitiveView;
import gay.sylv.conduit.api.renderer.mesh.VertexExtension;

public class MutablePrimitiveViewImpl extends PrimitiveViewImpl implements MutablePrimitiveView {
	public MutablePrimitiveViewImpl(RenderPipeline pipeline, int vertexExtensions) {
		super(pipeline, vertexExtensions);
	}

	/// Special cases for vanilla vertex extensions.
	private static int vertexExtensionsFromVanillaPipeline(RenderPipeline pipeline) {
		return 0;
	}

	@Override
	public MutablePrimitiveView pipeline$conduit(RenderPipeline pipeline, int vertexExtensions) {
		if (!PrimitiveObjectsImpl.hasPipeline(pipeline)) {
			throw new IllegalArgumentException("Pipeline " + pipeline.getLocation() + " is unregistered");
		}

		this.initializePipeline(pipeline);
		return this;
	}

	@Override
	public MutablePrimitiveView pipeline$conduit(RenderPipeline pipeline) {
		this.pipeline$conduit(pipeline, 0);
		return this;
	}

	@Override
	public MutablePrimitiveView atlas$conduit(TextureAtlas atlas) {
		PrimitiveHeaders.ATLAS.set(this.data, atlas);
		return this;
	}

	@Override
	public MutablePrimitiveView sprite$conduit(TextureAtlasSprite sprite) {
		TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(sprite.atlasLocation());

		if (!PrimitiveObjectsImpl.hasAtlas(atlas)) {
			throw new IllegalArgumentException("Texture atlas " + atlas.location() + " is unregistered");
		}

		PrimitiveHeaders.ATLAS.set(this.data, atlas);

		switch (this.pipeline.getVertexFormatMode().primitiveLength) {
			case 4 -> {
				this.uv(0, sprite.getU0(), sprite.getV0());
				this.uv(1, sprite.getU1(), sprite.getV0());
				this.uv(2, sprite.getU1(), sprite.getV1());
				this.uv(3, sprite.getU0(), sprite.getV1());
			}
			case 3 -> {
				this.uv(0, sprite.getU0(), sprite.getV0());
				this.uv(1, sprite.getU1(), sprite.getV0());
				this.uv(2, sprite.getU(0.5f), sprite.getV(0.5f));
			}
			default -> throw new UnsupportedOperationException("Sprites cannot be applied to primitives of less than 3 points");
		}

		return this;
	}

	@Override
	public MutablePrimitiveView pos(int vertexIndex, float x, float y, float z) {
		int offset = this.getOffset(vertexIndex, VertexFormatElement.POSITION);
		this.data[offset] = floatToIntBits(x);
		this.data[offset + 1] = floatToIntBits(y);
		this.data[offset + 2] = floatToIntBits(z);
		return this;
	}

	@Override
	public MutablePrimitiveView color(int vertexIndex, int color) {
		int offset = this.getOffset(vertexIndex, VertexFormatElement.COLOR);
		this.data[offset] = color;
		return this;
	}

	@Override
	public MutablePrimitiveView uv(int vertexIndex, float u, float v) {
		int offset = this.getOffset(vertexIndex, VertexFormatElement.UV);
		this.data[offset] = floatToIntBits(u);
		this.data[offset + 1] = floatToIntBits(v);
		return this;
	}

	@Override
	public MutablePrimitiveView lightmap(int vertexIndex, int lightmap) {
		int offset = this.getOffset(vertexIndex, VertexExtension.LIGHTMAP);
		this.data[offset] = lightmap;
		return this;
	}

	@Override
	public MutablePrimitiveView normal(int vertexIndex, float x, float y, float z) {
		int offset;

		if (this.hasExtension(VertexExtension.NORMAL)) {
			offset = this.getOffset(vertexIndex, VertexExtension.NORMAL);
		} else {
			offset = this.getOffset(vertexIndex, VertexFormatElement.NORMAL);
		}

		this.data[offset] = floatToIntBits(x);
		this.data[offset + 1] = floatToIntBits(y);
		this.data[offset + 2] = floatToIntBits(z);
		return this;
	}

	@Override
	public MutablePrimitiveView nominalFace(@Nullable Direction face) {
		PrimitiveHeaders.NOMINAL_FACE.set(this.data, face);
		return this;
	}

	@Override
	public MutablePrimitiveView cullFace(@Nullable Direction face) {
		PrimitiveHeaders.CULL_FACE.set(this.data, face);
		return this;
	}

	@Override
	public MutablePrimitiveView emissive(boolean emissive) {
		PrimitiveHeaders.EMISSIVE.set(this.data, emissive);
		return this;
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

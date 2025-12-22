package gay.sylv.conduit.api.renderer.mesh;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.texture.TextureAtlas;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;

/// An extension of [QuadView].
@ApiStatus.NonExtendable
public interface PrimitiveView extends QuadView {
	/// Gets the [RenderPipeline] associated with this quad.
	RenderPipeline pipeline$conduit();

	/// Gets the [atlas][TextureAtlas] of this quad.
	TextureAtlas atlas$conduit();

	/// Gets how many vertices this primitive has.
	/// @see com.mojang.blaze3d.vertex.VertexFormat.Mode#primitiveLength
	int primitiveLength$conduit();

	/// @deprecated Use [#atlas$conduit()]
	@SuppressWarnings("NullableProblems") // unsupported in FRAPI, must be nullable
	@Override
	@Nullable
	@Deprecated
	default QuadAtlas atlas() {
		return QuadAtlas.of(this.atlas$conduit().location());
	}
}

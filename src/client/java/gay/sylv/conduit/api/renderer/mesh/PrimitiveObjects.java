package gay.sylv.conduit.api.renderer.mesh;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.renderer.texture.TextureAtlas;

import gay.sylv.conduit.impl.renderer.mesh.PrimitiveObjectsImpl;

/// Utility class for registering objects such as
/// [TextureAtlas] or [RenderPipeline] for use with primitives.
///
/// @apiNote Registration is required before using certain
/// objects in primitives. This is due to their representation
/// being too large under normal circumstances.
///
/// @implNote Conduit additionally imposes limits on how many
/// objects of each type may be registered. See the methods'
/// documentation for more information.
///
/// @see PrimitiveView
@ApiStatus.Experimental // not sure if I want to keep this as API or not
public final class PrimitiveObjects {
	private PrimitiveObjects() {
	}

	/// @implSpec {@linkplain TextureAtlas Atlases} are
	/// limited to **256** entries.
	public static void registerAtlas(TextureAtlas atlas) {
		PrimitiveObjectsImpl.registerAtlas(atlas);
	}

	/// @implSpec {@linkplain RenderPipeline Pipelines} are
	/// limited to **256** entries.
	public static void registerPipeline(RenderPipeline pipeline) {
		PrimitiveObjectsImpl.registerPipeline(pipeline);
	}
}

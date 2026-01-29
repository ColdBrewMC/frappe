package gay.sylv.conduit.api.ext.fabric_renderer;

import static gay.sylv.conduit.impl.base.ConduitInitializer.modId;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

import gay.sylv.conduit.api.base.extension.RendererExtension;

/// Provides more information about and centralizes access to the [Renderer] interface in FRAPI.
///
/// This extension is always implemented and may be used by other extensions.
///
/// Implementations of this extension should also extend [DefaultFabricRendererExtension] as it
/// provides good default behavior.
///
/// @apiNote This is what is known as a Core Extension. It is required for Conduit's operation and
/// may not be entirely located in its own module. In the case of `fabric-renderer`, the majority of
/// its code is in the `conduit-base` module.
public interface FabricRendererExtension extends RendererExtension {
	@Override
	default Identifier id() {
		return modId("fabric-renderer");
	}

	/// @return the unique (mod) ID of the [Renderer].
	@ApiStatus.OverrideOnly
	String getRendererId();

	/// @return an instance of [ConduitRenderer] based on [Renderer].
	@ApiStatus.OverrideOnly
	ConduitRenderer getRenderer(Renderer renderer);
}

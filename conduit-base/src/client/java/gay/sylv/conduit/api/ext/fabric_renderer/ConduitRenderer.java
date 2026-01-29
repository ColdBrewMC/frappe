package gay.sylv.conduit.api.ext.fabric_renderer;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

import gay.sylv.conduit.api.base.extension.RendererExtension;
import gay.sylv.conduit.impl.base.extension.ExtensionRegistryImpl;

/// @see FabricRendererExtension
public interface ConduitRenderer {
	/// @apiNote Do not call this method during mod initialization! Use [RendererRegistryEvents.BeforeRendererRegistry]
	/// to ensure a [Renderer] has been registered already.
	static ConduitRenderer get() {
		return ((FabricRendererExtension) ExtensionRegistryImpl.getExtensionOrThrow(ExtensionRegistryImpl.getId(FabricRendererExtension.class))).getRenderer(Renderer.get());
	}

	/// @return the unique (mod) ID of this [Renderer].
	default String id() {
		throw new IllegalStateException("Implemented via Mixin.");
	}

	/// @param id the [Identifier] of the [RendererExtension].
	/// @return whether the [RendererExtension] is loaded.
	default boolean isExtensionLoaded(Identifier id) {
		return ExtensionRegistryImpl.isLoaded(id);
	}

	/// @param <T> the type of the [RendererExtension].
	/// @param clazz the [Class] of the [RendererExtension].
	/// @return whether the [RendererExtension] is loaded.
	default <T extends RendererExtension> boolean isExtensionLoaded(Class<T> clazz) {
		return isExtensionLoaded(ExtensionRegistryImpl.getId(clazz));
	}

	/// @param id the [Identifier] of the [RendererExtension].
	/// @return if present, the [RendererExtension] associated with the given [Identifier].
	/// @throws NullPointerException if the [RendererExtension] is not loaded.
	default RendererExtension getExtension(Identifier id) {
		return ExtensionRegistryImpl.getExtensionOrThrow(id);
	}

	/// @param <T> the type of the [RendererExtension].
	/// @param clazz the [Class] of the [RendererExtension].
	/// @return if present, the [RendererExtension] associated with the given [Class].
	/// @throws NullPointerException if the [RendererExtension] is not loaded.
	default <T extends RendererExtension> T getExtension(Class<T> clazz) {
		//noinspection unchecked // Type T will always be the type T of Class<T> of parameter clazz.
		return (T) getExtension(ExtensionRegistryImpl.getId(clazz));
	}
}

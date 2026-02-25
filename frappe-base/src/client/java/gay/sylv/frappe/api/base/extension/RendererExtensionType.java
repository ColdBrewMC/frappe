package gay.sylv.frappe.api.base.extension;

import org.jetbrains.annotations.ApiStatus;

import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

/// A definition of an interface of [RendererExtension].
///
/// This is used to find implementations of [renderer extensions][RendererExtension].
public interface RendererExtensionType {
	/// Extensions may override this method in favor of using modules with separate mods.
	///
	/// The default value is this extension type's mod ID.
	///
	/// @return this extension type's unique ID.
	@ApiStatus.OverrideOnly
	default String id() {
		return ExtensionRegistryImpl.getEntrypoint(this.getClass()).getProvider().getMetadata().getId();
	}

	SupportTier supportTier();

	/// @return the type of the interface of [RendererExtension] that implementations will extend.
	@ApiStatus.OverrideOnly
	Class<? extends RendererExtension> implClass();
}

package gay.sylv.frappe.api.base.extension;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

import gay.sylv.frappe.impl.base.FrappeInitializer;

/// Helper for retrieving information about the currently selected renderer.
///
/// Base extension implementations adding support for renderers must mark their
/// implementations as entrypoints under `frappe-base:renderer_info` in the
/// `fabric.mod.json`.
public interface RendererInfo {
	/// @return the mod ID of the currently loaded renderer
	static String getModId() {
		//noinspection DataFlowIssue // checked when the RendererInfo is seeked out
		return FrappeInitializer.getRendererInfo().getLoadedRendererModId();
	}

	/// @implSpec If the currently loaded renderer is unsupported, implementations must
	/// return `null`.
	///
	/// @see #getModId()
	@ApiStatus.OverrideOnly
	@Nullable String getLoadedRendererModId();

	/// @return a new or existing instance of the [Renderer]
	@ApiStatus.OverrideOnly
	Renderer getRendererEarly();
}

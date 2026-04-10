package gay.sylv.frappe.mocha.impl.base;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.base.extension.RendererInfo;

public class MochaRendererInfo implements RendererInfo {
	@Override
	public @Nullable String getLoadedRendererModId() {
		boolean isSodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
		//noinspection UnstableApiUsage // instanceof check it's fine
		boolean isIndigoLoaded = Renderer.get() instanceof IndigoRenderer;
		return isSodiumLoaded ? "sodium" : isIndigoLoaded ? "fabric-renderer-indigo" : null;
	}
}

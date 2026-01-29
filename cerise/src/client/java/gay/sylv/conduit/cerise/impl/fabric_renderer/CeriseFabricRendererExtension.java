package gay.sylv.conduit.cerise.impl.fabric_renderer;

import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.conduit.api.ext.fabric_renderer.DefaultFabricRendererExtension;

public final class CeriseFabricRendererExtension extends DefaultFabricRendererExtension {
	@Override
	public int priority() {
		return COMPATIBILITY_PRIORITY;
	}

	@Override
	public String getRendererId() {
		if (FabricLoader.getInstance().isModLoaded("sodium")) {
			return "sodium";
		} else if (FabricLoader.getInstance().isModLoaded("fabric-renderer-indigo")) {
			return "fabric-renderer-indigo";
		} else {
			return super.getRendererId();
		}
	}
}

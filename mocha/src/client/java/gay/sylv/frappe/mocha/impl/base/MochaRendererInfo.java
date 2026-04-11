package gay.sylv.frappe.mocha.impl.base;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.impl.client.indigo.IndigoMixinConfigPlugin;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.base.extension.RendererInfo;

@SuppressWarnings("UnstableApiUsage")
public class MochaRendererInfo implements RendererInfo {
	static MethodHandle INDIGO_MCP_SHOULD_APPLY;

	static {
		try {
			INDIGO_MCP_SHOULD_APPLY = MethodHandles.privateLookupIn(IndigoMixinConfigPlugin.class, MethodHandles.lookup())
					.findStatic(IndigoMixinConfigPlugin.class, "shouldApplyIndigo", MethodType.methodType(boolean.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public @Nullable String getLoadedRendererModId() {
		boolean isSodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
		boolean isIndigoLoaded;

		try {
			isIndigoLoaded = (boolean) INDIGO_MCP_SHOULD_APPLY.invokeExact();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		return isSodiumLoaded ? "sodium" : isIndigoLoaded ? "fabric-renderer-indigo" : null;
	}

	@Override
	public Renderer getRendererEarly() {
		return IndigoRenderer.INSTANCE;
	}
}

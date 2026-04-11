package gay.sylv.frappe.mixin.base;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.base.extension.RendererReadyEntrypoint;
import gay.sylv.frappe.impl.base.FrappeInitializer;

@Mixin(Renderer.class)
public interface Mixin_Renderer {
	@Inject(method = "register", at = @At("RETURN"))
	private static void onRegister(
			Renderer renderer,
			CallbackInfo ci
	) {
		FabricLoader.getInstance()
				.invokeEntrypoints(
						"frappe-base:renderer_ready",
						RendererReadyEntrypoint.class,
						entrypoint -> entrypoint.onRendererReady(renderer)
				);
	}

	@WrapOperation(method = "get", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/client/renderer/RendererManager;getRenderer()Lnet/fabricmc/fabric/api/client/renderer/v1/Renderer;"))
	private static Renderer getAndRegister(Operation<Renderer> original) {
		try {
			return original.call();
		} catch (UnsupportedOperationException e) {
			Renderer renderer = FrappeInitializer.getRendererInfo().getRendererEarly();
			Renderer.register(renderer);
			return renderer;
		}
	}
}

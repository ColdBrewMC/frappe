package gay.sylv.frappe.mocha.mixin.indigo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.impl.client.indigo.Indigo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(Indigo.class)
public abstract class Mixin_Indigo {
	@WrapOperation(method = "onInitializeClient", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/client/renderer/v1/Renderer;register(Lnet/fabricmc/fabric/api/client/renderer/v1/Renderer;)V"))
	private void cancelRegistryForEarlyRegistry(
			Renderer renderer,
			Operation<Void> original
	) {
	}
}

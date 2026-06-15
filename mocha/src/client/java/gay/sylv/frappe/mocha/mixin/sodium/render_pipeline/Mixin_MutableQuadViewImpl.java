package gay.sylv.frappe.mocha.mixin.sodium.render_pipeline;

import net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.model.QuadViewImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.sylv.frappe.mocha.impl.indium.render_pipeline.Ext_QuadData;

@Mixin(MutableQuadViewImpl.class)
public abstract class Mixin_MutableQuadViewImpl extends QuadViewImpl implements Ext_QuadData {
	@Inject(method = "clear", at = @At("HEAD"))
	private void onClear(CallbackInfo ci) {
		this.frappe$vertexAttributes().clear();
		this.frappe$quadAttributes().clear();
	}
}

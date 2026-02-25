package gay.sylv.frappe.mocha.mixin.indigo.quad_view;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.quad_view.FrappeQuadEmitter;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MutableQuadViewImpl.class)
public abstract class Mixin_MutableQuadViewImpl implements FrappeMutableQuadView<MutableQuadViewImpl>, FrappeQuadEmitter<MutableQuadViewImpl> {
}

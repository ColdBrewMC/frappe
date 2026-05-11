package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderTextureSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkShaderTextureSlot.class)
public enum Mixin_ChunkShaderTextureSlot {
	@SuppressWarnings("AddedEnumConstantsNamePattern") // MCDev bug
	MOCHA_MATERIAL_INFO
}

/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderLoader;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.mocha.impl.Mocha;

@Mixin(ShaderLoader.class)
public abstract class Mixin_ShaderLoader {
	@WrapMethod(method = "getShaderSource")
	private static String useMochaShaderSource(
			Identifier name,
			Operation<String> original
	) {
		String path = String.format("/assets/%s/shaders/%s", name.getNamespace(), name.getPath());

		try {
			if (name.getNamespace().equals("sodium")) {
				return switch (name.getPath()) {
					case "blocks/block_layer_opaque.vsh", "blocks/block_layer_opaque.fsh", "include/chunk_vertex.glsl" -> {
						String altPath = String.format("/assets/mocha/shaders/%s", name.getPath());

						try (InputStream inputStream = Mocha.class.getResourceAsStream(altPath)) {
							yield new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
						}
					}
					default -> original.call(name);
				};
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read shader source for " + path, e);
		}

		return original.call(name);
	}
}

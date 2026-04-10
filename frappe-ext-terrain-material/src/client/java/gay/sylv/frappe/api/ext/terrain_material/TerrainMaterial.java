/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.terrain_material;

import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

/// A modification to terrain's vertex and fragment shaders.
///
/// Each [TerrainMaterial] **must** be
/// [registered][TerrainMaterialExtension#registerMaterial(TerrainMaterial)] before use.
///
/// This property may be set using [MQV_ExtTerrainMaterial#frappe$terrainMaterial(TerrainMaterial)].
@ApiStatus.NonExtendable
public interface TerrainMaterial {
	Identifier shaderId();

	/// A label used in debugging.
	String label();

	/// @see Complexity
	Complexity complexity();

	/// @see Complexity#SIMPLE
	default boolean simple() {
		return complexity().equals(Complexity.SIMPLE);
	}

	final class Builder {
		private final Identifier shaderId;
		private @Nullable String label;
		private @Nullable Complexity complexity;
		private @Nullable Function<RenderPipeline.Builder, RenderPipeline.Builder> renderPipelineModifier;
		private @Nullable Runnable preRenderPassState;
		private @Nullable Runnable postRenderPassState;
		private @Nullable Consumer<RenderPass> renderPassSetup;
		private @Nullable Consumer<RenderPass> renderPassCleanup;

		public Builder(Identifier shaderId) {
			this.shaderId = shaderId;
		}

		public static Builder of(Identifier shaderId) {
			return new Builder(shaderId);
		}

		/// @see #label()
		public Builder label(String label) {
			this.label = label;
			return this;
		}

		/// A complexity rating provides useful optimization hints for the renderer.
		/// @see #complexity()
		public Builder complexity(Complexity complexity) {
			this.complexity = complexity;
			return this;
		}

		/// A function that is used to modify the [render pipelines][RenderPipeline] of a complex material.
		public Builder renderPipelineModifier(Function<RenderPipeline.Builder, RenderPipeline.Builder> renderPipelineModifier) {
			this.renderPipelineModifier = renderPipelineModifier;
			return this;
		}

		/// A runnable that is invoked to set state (e.g. write to
		/// [dynamic uniforms][net.minecraft.client.renderer.DynamicUniforms]) required before or after
		/// the [RenderPass] of a complex material is initialized.
		public Builder renderPassState(Runnable preRenderPassState, Runnable postRenderPassState) {
			this.preRenderPassState = preRenderPassState;
			this.postRenderPassState = postRenderPassState;
			return this;
		}

		/// A consumer that is invoked to modify the [RenderPass] before or after a complex material
		/// renders.
		public Builder renderPassModifier(Consumer<RenderPass> renderPassSetup, Consumer<RenderPass> renderPassCleanup) {
			this.renderPassSetup = renderPassSetup;
			this.renderPassCleanup = renderPassCleanup;
			return this;
		}

		public TerrainMaterial build() {
			if (label == null) {
				label = shaderId.toString();
			}

			if (complexity == null) {
				throw new NullPointerException("Error while building TerrainMaterial: terrain materials require a complexity rating");
			}

			if (renderPipelineModifier != null && !complexity.equals(Complexity.ISOLATE)) {
				throw new IllegalArgumentException("Error while building TerrainMaterial: terrain materials with RenderPipeline modifiers must be rated isolate");
			}

			if (preRenderPassState != null && !complexity.equals(Complexity.ISOLATE)) {
				throw new IllegalArgumentException("Error while building TerrainMaterial: terrain materials with pre-RenderPass state must be rated isolate");
			}

			if (renderPassSetup != null && !complexity.equals(Complexity.ISOLATE)) {
				throw new IllegalArgumentException("Error while building TerrainMaterial: terrain materials with RenderPass modifiers must be rated isolate");
			}

			return TerrainMaterialExtension.get()
					.createChunkLayer(
							shaderId,
							label,
							complexity,
							renderPipelineModifier,
							preRenderPassState,
							postRenderPassState,
							renderPassSetup,
							renderPassCleanup
					);
		}
	}

	/// A measure of how independent and resource intensive a particular material is.
	///
	/// @see #SIMPLE
	/// @see #COMPLEX
	/// @see #ISOLATE
	enum Complexity {
		/// This material's shader does not branch and uses only simple operations that may be repeated
		/// across all terrain.
		SIMPLE,
		/// This material's shader uses more complicated operations that may be repeated across all
		/// other terrain with complex materials.
		COMPLEX,
		/// This material's shader uses branches, uses very complicated operations, or requires extra
		/// uniforms, all of which absolutely must not be repeated for other terrain. Most
		/// implementations will put this material on its own
		/// [chunk layer][net.minecraft.client.renderer.chunk.ChunkSectionLayer] or even its own
		/// [pass][net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup], making it isolate.
		///
		/// Up to 7 isolate materials are guaranteed to be supported in compliant implementations.
		/// Registering any more materials is implementation defined behavior.
		ISOLATE,
	}
}

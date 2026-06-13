/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import java.util.Map;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline;

public class IndigoTerrainMaterialInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LevelRenderEvents.START_MAIN.register(context -> {
			for (FrappeRenderPipeline pipeline : FrappeRenderPipeline.getAllPipelines()) {
				for (Map.Entry<String, FrappeRenderPipeline.UniformGetter<?>> entry : pipeline.uniformGetters().entrySet()) {
					if (!IndigoPipelineUniform.INSTANCES.containsKey(entry.getKey())) {
						IndigoPipelineUniform.INSTANCES.put(entry.getKey(), new IndigoPipelineUniform<>(entry.getKey(), pipeline.uniformDataTypes().get(entry.getKey())));
					}

					//noinspection unchecked // Object upcast
					IndigoPipelineUniform<Object> uniform = (IndigoPipelineUniform<Object>) IndigoPipelineUniform.INSTANCES.get(entry.getKey());
					uniform.update(entry.getValue().getValue(context.gameRenderer().getGameRenderState()));
				}
			}
		});
	}
}

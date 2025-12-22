package gay.sylv.conduit.impl.renderer.mesh;

import java.util.Objects;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.AtlasManager;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadAtlas;

public final class PrimitiveObjectsImpl {
	private static final TextureAtlas[] ATLASES = new TextureAtlas[256];
	private static final RenderPipeline[] PIPELINES = new RenderPipeline[256];
	private static final Object2IntMap<TextureAtlas> ATLASES_MAP = new Object2IntOpenHashMap<>();
	private static final Object2IntMap<RenderPipeline> PIPELINES_MAP = new Object2IntOpenHashMap<>();
	private static int ATLASES_LENGTH = 0;
	private static int PIPELINES_LENGTH = 0;

	static {
		// register vanilla defaults
		AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
		registerPipeline(RenderPipelines.SOLID_TERRAIN);
		registerPipeline(RenderPipelines.TRIPWIRE_TERRAIN);
		registerPipeline(RenderPipelines.TRANSLUCENT_TERRAIN);
		registerPipeline(RenderPipelines.CUTOUT_TERRAIN);
		registerPipeline(RenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL);
		registerAtlas(atlasManager.getAtlasOrThrow(QuadAtlas.BLOCK.getTextureId()));
		registerAtlas(atlasManager.getAtlasOrThrow(QuadAtlas.ITEM.getTextureId()));
	}

	private PrimitiveObjectsImpl() {
	}

	public static void registerAtlas(TextureAtlas atlas) {
		ATLASES[ATLASES_LENGTH] = atlas;
		ATLASES_MAP.put(atlas, ATLASES_LENGTH);
		ATLASES_LENGTH++;
	}

	public static void registerPipeline(RenderPipeline pipeline) {
		PIPELINES[PIPELINES_LENGTH] = pipeline;
		PIPELINES_MAP.put(pipeline, PIPELINES_LENGTH);
		PIPELINES_LENGTH++;
	}

	public static TextureAtlas getAtlas(int index) {
		return Objects.requireNonNull(ATLASES[index]);
	}

	public static RenderPipeline getPipeline(int index) {
		return Objects.requireNonNull(PIPELINES[index]);
	}

	public static int getAtlasIndex(TextureAtlas atlas) {
		return ATLASES_MAP.getInt(atlas);
	}

	public static int getPipelineIndex(RenderPipeline pipelines) {
		return PIPELINES_MAP.getInt(pipelines);
	}

	public static boolean hasAtlas(TextureAtlas atlas) {
		return ATLASES_MAP.containsKey(atlas);
	}

	public static boolean hasPipeline(RenderPipeline pipeline) {
		return PIPELINES_MAP.containsKey(pipeline);
	}
}

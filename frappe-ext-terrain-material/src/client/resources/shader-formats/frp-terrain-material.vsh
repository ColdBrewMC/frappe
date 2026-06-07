#version 330

/*
 * Custom texture coordinates.
 */
out vec2 ftm_texCoord, ftm_vertUv;

/*
 * The ambient occlusion multiplier on this vertex.
 */
out float ftm_vertAo;

/*
 * The block atlas's texture size.
 */
const ivec2 ftm_blockAtlasTextureSize;

/*
 * Called before ambient occlusion is applied.
 *
 * Modify ftm_vertAo in this method.
 *
 * Note: some renderers do not support separated Ambient Occlusion values (e.g. Vanilla Minecraft).
 * In such cases, ftm_vertAo will be assigned 1.0 (multiplicative identity), and
 * ambient occlusion will be applied directly to frp_vertColor.
 */
void ftm_setupAoVertex();

/*
 * Called after ambient occlusion is applied.
 */
void ftm_applyAoVertex();

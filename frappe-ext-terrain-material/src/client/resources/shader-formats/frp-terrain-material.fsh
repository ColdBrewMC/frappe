#version 330

/*
 * Custom texture coordinates.
 */
const in vec2 ftm_texCoord, ftm_vertUv;

/*
 * The block atlas texture.
 */
const sampler2D ftm_blockAtlasTexture;

/*
 * The block atlas's texture size.
 */
const ivec2 ftm_blockAtlasTextureSize;

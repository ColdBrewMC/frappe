#version 330

void frp_inputVertex() {
	float interp = mochaTest_glintSpeed * 125 * frp_levelTime;
	float dx = (mix(-64, 64, mod(interp, 1)) / ftm_blockAtlasTextureSize.x);
	float dy = (mix(-64, 64, mod(interp, 1)) / ftm_blockAtlasTextureSize.y);
	ftm_texCoord.x += dx; // testing aliases
	ftm_vertUv.y += dy;
}

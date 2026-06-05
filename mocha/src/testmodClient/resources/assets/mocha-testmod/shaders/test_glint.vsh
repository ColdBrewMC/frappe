#version 330

#define PI 3.141592653589793238462643
#define GLINT_SPEED 1.0

vec4 rotateX(vec4 vec, float angle) {
	return vec * mat4(
		1.0, 		0.0, 		 0.0, 0.0,
		0.0, cos(angle), -sin(angle), 0.0,
		0.0, sin(angle),  cos(angle), 0.0,
		0.0, 		0.0, 		 0.0, 1.0
	);
}

vec4 rotateY(vec4 vec, float angle) {
	return vec * mat4(
	cos(angle),  0.0, sin(angle),  0.0,
	0.0, 		 1.0, 		 0.0,  0.0,
	0.0, -sin(angle), cos(angle),  0.0,
	0.0, 		 0.0, 		 0.0,  1.0
	);
}

void frp_inputVertex() {
	float interp = mochaTest_glintSpeed * 125 * frp_levelTime;
	float dx = (mix(-64, 64, mod(interp, 1)) / ftm_blockAtlasTextureSize.x);
	float dy = (mix(-64, 64, mod(interp, 1)) / ftm_blockAtlasTextureSize.y);
	ftm_vertUv.x += dx;
	ftm_vertUv.y += dy;
}

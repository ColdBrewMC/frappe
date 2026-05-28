#version 330

#ifdef _FRAPPE_COMPLEX_MATERIAL

#define PI 3.141592653589793238462643
#define GLINT_SPEED 1.0

#ifdef _FRAPPE_ISOLATE_MATERIAL // TODO make this unnecessary via regex shader transforming
//#import <mocha-testmod:dynamictransforms.glsl>
#endif

#if false
uniform vec2 frp_exp_AtlasTextureSize;
uniform float frp_exp_LevelTime;
#endif

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

vec2 frp_modify_uv(vec2 oldUv, float isMaterial) {
	vec4 uv = vec4(oldUv, 0.0, 1.0);
	#ifdef _FRAPPE_ISOLATE_MATERIAL
//	uv = rotateY(uv, PI);
//	uv = rotateX(uv, -PI * 2.0);
//	uv = vec4(uv.x, -uv.y, uv.yw);
//	uv = (TextureMat * 0.5 * uv);
//	uv /= vec4(TextureSize / 128.0, 1.0, 1.0);
	#endif
	float interp = GLINT_SPEED * 125 * frp_exp_LevelTime;
	float dx = (mix(-64, 64, mod(interp, 1)) / frp_exp_AtlasTextureSize.x);
	float dy = (mix(-64, 64, mod(interp, 1)) / frp_exp_AtlasTextureSize.y);
	uv.x += dx;
	uv.y += dy;
	return uv.xy;
}

#endif

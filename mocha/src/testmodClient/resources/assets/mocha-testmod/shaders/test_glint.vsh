#version 330

#define PI 3.141592653589793238462643

#ifdef _FRAPPE_ISOLATE_MATERIAL // TODO make this unnecessary via regex shader transforming
#moj_import <mocha-testmod:dynamictransforms.glsl>
#endif

#if false
uniform mat4 TextureMat;
uniform vec2 TextureSize;
uniform float GameTime;
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

vec2 frappe_modify_uv(vec2 oldUv, float isMaterial) {
	vec4 uv = vec4(oldUv, 0.0, 1.0);
	#ifdef _FRAPPE_ISOLATE_MATERIAL
	uv = rotateY(uv, PI);
	uv = rotateX(uv, -PI * 2.0);
	uv = vec4(uv.x, -uv.y, uv.yw);
	uv = (TextureMat * 0.5 * uv);
	uv /= vec4(TextureSize / 128.0, 1.0, 1.0);
	#endif
	return uv.xy;
}

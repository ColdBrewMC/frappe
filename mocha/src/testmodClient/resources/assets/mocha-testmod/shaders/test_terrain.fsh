#version 330

#define PI 3.141592653589793238462643
#define COMPLEX_SPEED 0.25

#if false
uniform float GameTime;
#endif

vec4 frappe_simple_pre_fragment(vec4 color, float isMaterial) {
	vec3 extra = vec3(color.r * 1.57, 0.0, color.b * 1.28) * 0.5 * isMaterial;
	return vec4(color.rgb + extra, color.a);
}

vec4 frappe_pre_fragment(vec4 color, float isMaterial) {
	float isNotMaterial = (1.0 - isMaterial);
	float tau = (sin(COMPLEX_SPEED * 500.0 * GameTime * 2.0 * PI) + 1.0) / 2.0;
	vec2 extra = vec2(color.g * 1.57, color.b * 1.28);
	extra *= tau;
	return vec4(color.r, extra + vec2(0.25, 0.25), color.a) * isMaterial + color * isNotMaterial;
}

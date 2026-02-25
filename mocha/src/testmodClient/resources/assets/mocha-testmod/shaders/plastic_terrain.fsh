#version 330

#define PI 3.141592653589793238462643
#define SPEED 0.25

#if false
uniform float GameTime;
#endif

vec4 frappe_pre_fragment(vec4 color, float isMaterial) {
	float isNotMaterial = (1.0 - isMaterial);
	float tau = sin(SPEED * 500 * GameTime * 2.0 * PI) + 0.5;
	vec2 extra = vec2(color.g * 1.57, color.b * 1.28);
	extra *= tau;
	return vec4(color.r, extra + vec2(0.25, 0.25), color.a) * isMaterial + color * isNotMaterial;
}

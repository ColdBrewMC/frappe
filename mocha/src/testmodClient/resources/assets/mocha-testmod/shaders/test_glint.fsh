#version 330

#define PI 3.141592653589793238462643
#define GLINT_SPEED 1.0

#if false
uniform float GameTime;
uniform float GlintAlpha;
uniform sampler2D Sampler0;
uniform ivec2 TextureSize;

in vec2 frappeUV;
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL
vec4 frappe_pre_fragment(vec4 color, float isMaterial) {
	float interp = GLINT_SPEED * 125 * GameTime * 2.0 * PI;
	float tauX = (sin(interp) + 1.0) / 2.0;
	float tauY = (cos(interp) + 1.0) / 2.0;
	float dx = (mix(-32, 32, tauX) / TextureSize.x);
	float dy = (mix(-32, 32, tauY) / TextureSize.y);
	vec2 dxdy = vec2(dx, dy);
	vec4 glintColor = texture(Sampler0, frappeUV + dxdy) * GlintAlpha * 0.5;
	return vec4(color.rgb + glintColor.rgb * isMaterial, color.a);
}
#endif

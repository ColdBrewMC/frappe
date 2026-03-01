#version 330

#define GLINT_SPEED 1.0

#if false
uniform float GameTime;
uniform float GlintAlpha;
uniform sampler2D Sampler0;
uniform ivec2 TextureSize;

in vec2 frappeUV;
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL // TODO make this unnecessary via the regex shader transformer
vec4 frappe_pre_fragment(vec4 color, float isMaterial) {
	float interp = GLINT_SPEED * 62.5 * GameTime * 2.0 * PI;
	float tauX = (sin(interp) + 1.0) / 2.0;
	float tauY = (cos(interp) + 1.0) / 2.0;
	float dx = (mix(0, 112, tauX) / TextureSize.x);
	float dy = (mix(0, 112, tauY) / TextureSize.y);
	vec2 dxdy = vec2(dx, dy);
	vec4 glintColor = texture(Sampler0, frappeUV + dxdy) * GlintAlpha;
	color.rgb = mix(color.rgb, (glintColor.rgb * glintColor.rgb) + (color.rgb), isMaterial);
	return vec4(color.rgb + glintColor.rgb * 0.25 * isMaterial, color.a);
//	vec4 glintColor = texture(Sampler0, frappeUV) * GlintAlpha;
//	vec4 outColor = color;
//	outColor.rgb = mix(color.rgb, (glintColor.rgb * glintColor.rgb) + color.rgb, isMaterial);
//	outColor.a = mix(color.a, color.a, isMaterial);
//	return outColor;
}
#endif

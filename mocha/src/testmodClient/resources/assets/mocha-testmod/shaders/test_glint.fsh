#version 330

#define GlintAlpha 0.5

#if false
//uniform float GlintAlpha;
uniform sampler2D Sampler0;

in vec2 v_FrappeUV;
#endif

//#ifdef _FRAPPE_COMPLEX_MATERIAL // TODO make this unnecessary via the regex shader transformer
vec4 frappe_simple_pre_fragment(vec4 color, float isMaterial) {
//	vec4 glintColor = texture(Sampler0, v_FrappeUV) * GlintAlpha;
	vec4 glintColor = vec4(0.5, 0, 0.5, 1.0) * GlintAlpha;
	vec4 outColor = color;
	outColor.rgb = mix(color.rgb, (glintColor.rgb * sqrt(glintColor.rgb)) + color.rgb, isMaterial);
	outColor.a = mix(color.a, color.a, isMaterial);
	return outColor;
}
//#endif

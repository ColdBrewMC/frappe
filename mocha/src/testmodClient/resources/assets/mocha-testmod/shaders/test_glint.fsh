#version 330

#if false
uniform float frp_exp_GlintAlpha;
uniform sampler2D frp_exp_BlockAtlasTexture;

in vec2 v_frp_exp_UV;
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL // TODO make this unnecessary via the regex shader transformer
vec4 frp_pre_fragment(vec4 color, float isMaterial) {
	vec4 glintColor = texture(frp_exp_BlockAtlasTexture, v_frp_exp_UV) * frp_exp_GlintAlpha;
//	vec4 glintColor = vec4(0.5, 0, 0.5, 1.0) * GlintAlpha;
	vec4 outColor = color;
	outColor.rgb = mix(color.rgb, (glintColor.rgb * sqrt(glintColor.rgb)) + color.rgb, isMaterial);
	outColor.a = mix(color.a, color.a, isMaterial);
	return outColor;
}
#endif

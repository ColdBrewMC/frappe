#version 330

void frp_inputFragment() {
	float isMaterial = float(frp_quadMaterialId == FRP_MATERIAL_ID);
	vec4 glintColor = texture(ftm_blockAtlasTexture, ftm_vertUv) * mochaTest_glintAlpha;
	vec4 color = frp_fragColor;
	vec4 outColor = color;
	outColor.rgb = mix(color.rgb, (glintColor.rgb * sqrt(glintColor.rgb)) + color.rgb, isMaterial);
	outColor.a = mix(color.a, color.a, isMaterial);
	frp_fragColor = outColor;
}

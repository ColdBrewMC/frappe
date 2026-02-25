#version 330

vec4 frappe_pre_fragment(vec4 color, float isMaterial) {
//	vec3 extra = color.rgb * isMaterial;
	return vec4(color.rgb * isMaterial, color.a);
}

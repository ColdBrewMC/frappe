#version 330

#define PI 3.141592653589793238462643
#define COMPLEX_SPEED 0.25

void frp_inputFragment() {
	float isMaterial = float(frp_quadMaterialId == FRP_MATERIAL_ID);
	vec4 color = frp_fragColor;
	vec3 extra = vec3(color.r * 1.57, 0.0, color.b * 1.28) * 0.5 * isMaterial;
	frp_fragColor = vec4(color.rgb + extra, color.a);
}

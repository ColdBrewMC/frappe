#version 330 core

#custom import <sodium:include/fog.glsl>
#custom import <sodium:include/chunk_vertex.glsl>
#custom import <sodium:include/chunk_matrices.glsl>

out vec4 v_Color;
out vec2 v_TexCoord;

flat out uint v_Material;

#ifdef USE_FOG
out vec2 v_FragDistance;
out float fadeFactor;
#endif
#ifdef _FRAPPE_COMPLEX_MATERIAL
out vec2 v_FrappeUV;
#endif
flat out uint v_FrappeMaterialId;

// Extra uniforms included by Frappé
uniform vec4 u_FogColor; // The color of the shader fog

uniform vec3 u_RegionOffset;
uniform vec2 u_TexCoordShrink;
uniform vec2 u_FrappeCompatTextureSize;
uniform float u_FrappeCompatLevelTime;

uniform sampler2D u_LightTex; // The light map texture sampler

uniform int u_CurrentTime;
uniform float u_FadePeriodInv;

layout(std140) uniform ChunkData {
	ivec4 u_chunkFades[64]; // Packing into ivec4 is needed to avoid wasting 3KB...
};

#custom frp_imports

uvec3 _get_relative_chunk_coord(uint pos) {
	// Packing scheme is defined by LocalSectionIndex
	return uvec3(pos) >> uvec3(5u, 0u, 2u) & uvec3(7u, 3u, 7u);
}

vec3 _get_draw_translation(uint pos) {
	return _get_relative_chunk_coord(pos) * vec3(16.0);
}

void main() {
	// ==== UniformGetter Initialization ====
	frp_fogColor = u_FogColor;
	frp_levelTime = u_FrappeCompatLevelTime;
	frp_atlasTextureSize = ivec2(int(u_FrappeCompatTextureSize.x), int(u_FrappeCompatTextureSize.y));

	// ==== Vertex Input ====
	_vert_init();
	frp_vertColor = _vert_color;
	frp_quadMaterialId = _frappe_material_id;

	// Transform the chunk-local vertex position into world model space
	vec3 translation = u_RegionOffset + _get_draw_translation(_draw_id);
	vec3 position = _vert_position + translation;

	frp_vertPosition = position;

	#ifdef USE_FOG
	v_FragDistance = getFragDistance(position);
	frp_vertDistance = v_FragDistance.y;
	#else
	frp_vertDistance = 0.0;
	#endif

	#ifdef _FRAPPE_COMPLEX_MATERIAL
	ftm_vertUv = _vert_frappe_uv;
	#endif

	frp_inputVertex();

	#ifdef USE_FOG
	int chunkId = int(_draw_id);
	int chunkFade = u_chunkFades[chunkId >> 2][chunkId & 3];
	int fadeTime = u_CurrentTime - chunkFade;
	float elapsed = float(fadeTime);
	float fade = clamp(float(u_CurrentTime - chunkFade) * u_FadePeriodInv, 0.0, 1.0);
	fadeFactor = (chunkFade < 0) ? 1.0 : fade;
	#endif

	// Add the light color to the vertex color, and pass the texture coordinates to the fragment shader
	frp_vertColor = frp_vertColor * texture(u_LightTex, _vert_tex_light_coord);
	v_TexCoord = (_vert_tex_diffuse_coord_bias * u_TexCoordShrink) + _vert_tex_diffuse_coord; // FMA for precision

	// ==== Vertex Output ====
	frp_outputVertex();
	v_Color = frp_vertColor;

	// Transform the vertex position into model-view-projection space
	gl_Position = u_ProjectionMatrix * u_ModelViewMatrix * vec4(frp_vertPosition, 1.0);

	v_Material = _material_params & 7u;

	// Unpack material ID byte
	v_FrappeMaterialId = frp_quadMaterialId;

	#ifdef _FRAPPE_COMPLEX_MATERIAL
	v_FrappeUV = ftm_vertUv;
	#endif
}

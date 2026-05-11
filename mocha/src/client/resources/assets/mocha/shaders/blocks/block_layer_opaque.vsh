#version 330 core

#import <sodium:include/fog.glsl>
#import <sodium:include/chunk_vertex.glsl>
#import <sodium:include/chunk_matrices.glsl>

// Sodium and Minecraft compatibility
#define Position position
#define Color v_Color;
#define UV0 v_TexCoord
#define TextureSize u_FrappeCompatTextureSize
#define ChunkVisibility fadeFactor
#define FogColor u_FogColor
#define GameTime float(u_CurrentTime)
#define moj_import import

#ifdef _FRAPPE_COMPLEX_MATERIAL
in vec2 _vert_frappe_uv;
#endif

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

uniform vec3 u_RegionOffset;
uniform vec2 u_TexCoordShrink;
uniform vec2 u_FrappeCompatTextureSize;
uniform vec3 u_MochaCameraOffset;

uniform sampler2D u_LightTex; // The light map texture sampler
uniform sampler2D u_MochaTex; // We pack material info into an int for each block of each chunk

uniform int u_CurrentTime;
uniform float u_FadePeriodInv;

layout(std140) uniform ChunkData {
	ivec4 u_chunkFades[64]; // Packing into ivec4 is needed to avoid wasting 3KB...
};

#import <mocha:vertex.glsl>

uvec3 _get_relative_chunk_coord(uint pos) {
	// Packing scheme is defined by LocalSectionIndex
	return uvec3(pos) >> uvec3(5u, 0u, 2u) & uvec3(7u, 3u, 7u);
}

vec3 _get_draw_translation(uint pos) {
	return _get_relative_chunk_coord(pos) * vec3(16.0);
}

void main() {
	_vert_init();

	// Transform the chunk-local vertex position into world model space
	vec3 translation = u_RegionOffset + _get_draw_translation(_draw_id);
	vec3 position = _vert_position + translation;

	#ifdef USE_FOG
	v_FragDistance = getFragDistance(position);

	int chunkId = int(_draw_id);
	int chunkFade = u_chunkFades[chunkId >> 2][chunkId & 3];
	int fadeTime = u_CurrentTime - chunkFade;
	float elapsed = float(fadeTime);
	float fade = clamp(float(u_CurrentTime - chunkFade) * u_FadePeriodInv, 0.0, 1.0);
	fadeFactor = (chunkFade < 0) ? 1.0 : fade;
	#endif

	// Transform the vertex position into model-view-projection space
	gl_Position = u_ProjectionMatrix * u_ModelViewMatrix * vec4(position, 1.0);

	// Add the light color to the vertex color, and pass the texture coordinates to the fragment shader
	v_Color = _vert_color * texture(u_LightTex, _vert_tex_light_coord);
	v_TexCoord = (_vert_tex_diffuse_coord_bias * u_TexCoordShrink) + _vert_tex_diffuse_coord; // FMA for precision

	v_Material = _material_params;

	#ifdef _FRAPPE_COMPLEX_MATERIAL
	v_FrappeUV = _frappe_modify_uv(_vert_frappe_uv);
	#endif
	// Unpack material ID byte
	vec3 block_position = position + u_MochaCameraOffset;
	block_position = vec3(round(block_position.x), round(block_position.y), round(block_position.z));
	uint _frappeBlockId = uint(int(block_position.x) & 0xF) | uint((int(block_position.y) & 0xF) << 4u) | uint((int(block_position.z) & 0xF) << 8u);
	// this SSBO is sus
	v_FrappeMaterialId = uint(texelFetch(u_MochaTex, ivec2(int(_frappeBlockId), chunkId), 0).r * 255.0);
	v_FrappeMaterialId = (uint(block_position.x) | v_FrappeMaterialId) & 255u;
}

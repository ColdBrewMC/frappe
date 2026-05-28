#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:chunksection.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:sample_lightmap.glsl>

// Standard FRP uniforms

// Experimental FRP uniforms
#define frp_exp_GlintAlpha GlintAlpha
#define frp_exp_FogColor FogColor
#define frp_exp_LevelTime GameTime
#define frp_exp_RGSSEnabled UseRgss
#define frp_exp_AtlasTextureSize TextureSize

// Standard FRP vertex data
#define v_frp_MaterialID (_vert_frappe_simple_material_info.x)

// Experimental FRP vertex data
#define v_frp_exp_ChunkFade ChunkVisibility
#define v_frp_exp_UV v_FrappeUV

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
#ifdef _FRAPPE_COMPLEX_MATERIAL
in vec2 _vert_frappe_uv;
#endif
in uvec2 _vert_frappe_simple_material_info;

uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
#ifdef _FRAPPE_COMPLEX_MATERIAL
out vec2 v_FrappeUV;
#endif
flat out uint v_FrappeMaterialId;

#moj_import <mocha:vertex.glsl>

void main() {
	vec3 pos = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
	gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

	sphericalVertexDistance = fog_spherical_distance(pos);
	cylindricalVertexDistance = fog_cylindrical_distance(pos);
	vertexColor = Color * sample_lightmap(Sampler2, UV2);
	texCoord0 = UV0;

	v_FrappeMaterialId = _vert_frappe_simple_material_info.x;

	#ifdef _FRAPPE_COMPLEX_MATERIAL
	v_FrappeUV = _frp_modify_uv(_vert_frappe_uv);
	#endif
}

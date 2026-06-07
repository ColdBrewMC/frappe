#version 330

#custom moj_import <minecraft:fog.glsl>
#custom moj_import <minecraft:globals.glsl>
#custom moj_import <minecraft:chunksection.glsl>
#custom moj_import <minecraft:projection.glsl>
#custom moj_import <minecraft:sample_lightmap.glsl>

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
out vec2 v_FrappeUV;
flat out uint v_FrappeMaterialId;

#custom frp_imports

void main() {
	// ==== Uniform Initialization ====
	frp_fogColor = FogColor;
	frp_levelTime = GameTime;
	frp_projectionMatrix = ProjMat;
	frp_modelViewMatrix = ModelViewMat;
	ftm_blockAtlasTextureSize = TextureSize;

	// ==== Vertex Input ====
	vec3 pos = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
	frp_vertPosition = vec4(pos, 1.0);
	frp_vertDistance = fog_spherical_distance(pos);
	frp_vertColor = Color * sample_lightmap(Sampler2, UV2);
	frp_quadMaterialId = _vert_frappe_simple_material_info.x;
	frp_texCoord = UV0;
	ftm_texCoord = _vert_frappe_uv;
	ftm_vertAo = 1.0;
	frp_inputVertex();

	// ==== Ambient Occlusion ====
	ftm_setupAoVertex();
	ftm_applyAoVertex();

	// ==== Vertex Output ====
	frp_vertPosition = frp_projectionMatrix * frp_modelViewMatrix * frp_vertPosition;
	frp_outputVertex();
	sphericalVertexDistance = frp_vertDistance;
	cylindricalVertexDistance = fog_cylindrical_distance(pos);
	vertexColor = frp_vertColor;
	texCoord0 = UV0;

	v_FrappeMaterialId = frp_quadMaterialId;

	gl_Position = frp_vertPosition;

	#ifdef _FRAPPE_COMPLEX_MATERIAL
	v_FrappeUV = ftm_vertUv;
	#else
//	v_FrappeUV = vec2(0.0);
	#endif
}

#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:chunksection.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:sample_lightmap.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
#ifdef _FRAPPE_COMPLEX_MATERIAL
in vec2 FrappeUV;
#endif
in uvec2 _frappe_simple_material_info;

uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
#ifdef _FRAPPE_COMPLEX_MATERIAL
out vec2 frappeUV;
#endif
flat out uint _frappe_material_id;

#moj_import <mocha:vertex.glsl>

void main() {
	vec3 pos = Position + (ChunkPosition - CameraBlockPos) + CameraOffset;
	gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

	sphericalVertexDistance = fog_spherical_distance(pos);
	cylindricalVertexDistance = fog_cylindrical_distance(pos);
	vertexColor = Color * sample_lightmap(Sampler2, UV2);
	texCoord0 = UV0;
	#ifdef _FRAPPE_COMPLEX_MATERIAL
	frappeUV = _frappe_modify_uv(FrappeUV);
	#endif
	_frappe_material_id = _frappe_simple_material_info.x;
}

#version 330

#ifdef _FRAPPE_MODIFY_UV
_FRAPPE_MODIFY_UV_FUNCTION_DEFS
#endif

#if false
in uvec2 _vert_frappe_simple_material_info;
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL
vec2 _frappe_modify_uv(vec2 uv) {
	#ifdef _FRAPPE_MODIFY_UV
	uv = _frappe_modify_uv__FRAPPE_MATERIAL_ID(uv, float(_vert_frappe_simple_material_info.x == _FRAPPE_MATERIAL_IDu));
	#endif
	return uv;
}
#endif

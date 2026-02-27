#version 330

#ifdef _FRAPPE_SIMPLE_PRE_FRAGMENT
_FRAPPE_SIMPLE_PRE_FRAGMENT_FUNCTION_DEFS
#endif

#ifdef _FRAPPE_PRE_FRAGMENT
_FRAPPE_PRE_FRAGMENT_FUNCTION_DEFS
#endif

#if false
in uint _frappe_material_id;
#endif

#ifdef _FRAPPE_SIMPLE_MATERIAL
vec4 _frappe_simple_pre_fragment(vec4 color) {
	#ifdef _FRAPPE_SIMPLE_PRE_FRAGMENT
	color = _frappe_simple_pre_fragment__FRAPPE_MATERIAL_ID(color, float(_frappe_material_id == _FRAPPE_MATERIAL_IDu));
	#endif
	return color;
}
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL
vec4 _frappe_pre_fragment(vec4 color) {
	#ifdef _FRAPPE_PRE_FRAGMENT
	color = _frappe_pre_fragment__FRAPPE_MATERIAL_ID(color, float(_frappe_material_id == _FRAPPE_MATERIAL_IDu));
	#endif
	return color;
}
#endif

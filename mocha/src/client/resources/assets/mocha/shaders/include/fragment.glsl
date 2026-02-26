#version 330

#ifdef _FRAPPE_SIMPLE_PRE_FRAGMENT
_FRAPPE_SIMPLE_PRE_FRAGMENT_FUNCTION_DEFS
#endif

#ifdef _FRAPPE_PRE_FRAGMENT
_FRAPPE_PRE_FRAGMENT_FUNCTION_DEFS
#endif

#if false
in ivec2 v_frappe_Extra;
#endif

vec4 _frappe_simple_pre_fragment(vec4 color) {
	#ifdef _FRAPPE_SIMPLE_PRE_FRAGMENT
	color = _frappe_simple_pre_fragment__FRAPPE_MATERIAL_ID(color, float(v_frappe_Extra.x == _FRAPPE_MATERIAL_ID));
	#endif
	return color;
}

vec4 _frappe_pre_fragment(vec4 color) {
	#ifdef _FRAPPE_PRE_FRAGMENT
	color = _frappe_pre_fragment__FRAPPE_MATERIAL_ID(color, float(v_frappe_Extra.x == _FRAPPE_MATERIAL_ID));
	#endif
	return color;
}

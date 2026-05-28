#version 330

#ifdef _FRP_SIMPLE_PRE_FRAGMENT
_FRP_SIMPLE_PRE_FRAGMENT_FUNCTION_DEFS
#endif

#ifdef _FRP_PRE_FRAGMENT
_FRP_PRE_FRAGMENT_FUNCTION_DEFS
#endif

#if false
flat in uint v_frp_MaterialID;
#endif

#ifdef _FRAPPE_SIMPLE_MATERIAL
vec4 _frp_simple_pre_fragment(vec4 color) {
	#ifdef _FRP_SIMPLE_PRE_FRAGMENT
	color = _frp_simple_pre_fragment__FRP_MATERIAL_ID(color, float(v_frp_MaterialID == _FRP_MATERIAL_IDu));
	#endif
	return color;
}
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL
vec4 _frp_pre_fragment(vec4 color) {
	#ifdef _FRP_PRE_FRAGMENT
	color = _frp_pre_fragment__FRP_MATERIAL_ID(color, float(v_frp_MaterialID == _FRP_MATERIAL_IDu));
	#endif
	return color;
}
#endif

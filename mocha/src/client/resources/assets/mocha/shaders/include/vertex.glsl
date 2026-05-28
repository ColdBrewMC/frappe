#version 330

#ifdef _FRP_MODIFY_UV
_FRP_MODIFY_UV_FUNCTION_DEFS
#endif

#if false
flat out uint v_frp_MaterialID;
#endif

#ifdef _FRAPPE_COMPLEX_MATERIAL
vec2 _frp_modify_uv(vec2 uv) {
	#ifdef _FRP_MODIFY_UV
	uv = _frp_modify_uv__FRP_MATERIAL_ID(uv, float(v_frp_MaterialID == _FRP_MATERIAL_IDu));
	#endif
	return uv;
}
#endif

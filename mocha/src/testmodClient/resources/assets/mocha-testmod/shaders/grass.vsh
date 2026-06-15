#version 330

void frp_inputVertex() {
	frp_vertColor.g += (frp_quadMaterialId == FRP_MATERIAL_ID) ? clamp(ftm_blockPos.x, -0.25, 0.25) : 0.0;
}

void ftm_setupAoVertex() {
}

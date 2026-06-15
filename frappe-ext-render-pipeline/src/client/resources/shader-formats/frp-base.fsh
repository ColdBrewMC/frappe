#version 330

/*
 * The color of fog.
 */
const vec4 frp_fogColor;

/*
 * The current level's discrete tick time.
 */
const float frp_levelTime;

/*
 * The interpolated fragment distance from the camera.
 */
const in float frp_vertDistance;

/*
 * The vertex color.
 */
const in vec4 frp_vertColor;

/*
 * The primary texture coordinates.
 */
const in vec2 frp_texCoord;

/*
 * The discrete block position.
 */
const in ivec3 frp_blockPos;

/*
 * The fragment's color interpolated from the vertex's color.
 *
 * Set this to change the fragment's final color.
 */
vec4 frp_fragColor;

/*
 * Called right after fragment inputs are processed,
 * before all fragment events will occur.
 */
void frp_inputFragment();

/*
 * Called after fog is applied to frp_fragColor.
 */
void frp_applyFogFragment();

/*
 * Called after all fragment events have occurred.
 */
void frp_outputFragment();

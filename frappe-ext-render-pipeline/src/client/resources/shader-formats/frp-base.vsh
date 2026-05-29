#version 330

/*
 * Defined if the current render pass is translucent.
 */
#define FRP_PASS_TRANSLUCENT

/*
 * Defined if the current render pass is transparent (also known as cutout).
 */
#define FRP_PASS_CUTOUT

/*
 * Defined if the current render pass has no alpha (also known as solid).
 */
#define FRP_PASS_SOLID

/*
 * The alpha value of glint as specified in the options.
 */
const float frp_glintAlpha;

/*
 * The color of fog.
 */
const vec4 frp_fogColor;

/*
 * The current level's discrete tick time.
 */
const float frp_levelTime;

/*
 * The main atlas's texture size.
 */
const ivec2 frp_atlasTextureSize;

/*
 * The vertex's position in the world.
 */
out vec3 frp_vertPosition;

/*
 * The vertex distance from the camera.
 */
out float frp_vertDistance;

/*
 * The vertex color.
 */
out vec4 frp_vertColor;

/*
 * Called right after vertex inputs are processed, before
 * any vertex events have occurred.
 */
void frp_inputVertex();

/*
 * Called when vertex outputs are set, after all other vertex
 * events have occurred.
 *
 * Pipeline shaders MUST set their custom vertex outputs in a
 * callback to this event.
 */
void frp_outputVertex();

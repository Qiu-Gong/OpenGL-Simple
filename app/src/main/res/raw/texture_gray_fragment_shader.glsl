precision mediump float;

uniform sampler2D u_Texture;
uniform vec3 u_Filter;
varying vec2 v_Coordinate;

void main()
{
    vec4 color = texture2D(u_Texture, v_Coordinate);
    float c = color.r * u_Filter.r + color.g * u_Filter.g + color.b * u_Filter.b;
    gl_FragColor = vec4(c, c, c, color.a);
}
precision mediump float;

uniform sampler2D u_Texture;
uniform vec3 u_Filter;
varying vec2 v_Coordinate;

void main()
{
    vec4 color = texture2D(u_Texture, v_Coordinate);
    color += texture2D(u_Texture, vec2(v_Coordinate.x-u_Filter.r, v_Coordinate.y-u_Filter.r));
    color += texture2D(u_Texture, vec2(v_Coordinate.x-u_Filter.r, v_Coordinate.y+u_Filter.r));
    color += texture2D(u_Texture, vec2(v_Coordinate.x+u_Filter.r, v_Coordinate.y-u_Filter.r));
    color += texture2D(u_Texture, vec2(v_Coordinate.x+u_Filter.r, v_Coordinate.y+u_Filter.r));
    color += texture2D(u_Texture, vec2(v_Coordinate.x-u_Filter.g, v_Coordinate.y-u_Filter.g));
    color += texture2D(u_Texture, vec2(v_Coordinate.x-u_Filter.g, v_Coordinate.y+u_Filter.g));
    color += texture2D(u_Texture, vec2(v_Coordinate.x+u_Filter.g, v_Coordinate.y-u_Filter.g));
    color += texture2D(u_Texture, vec2(v_Coordinate.x+u_Filter.g, v_Coordinate.y+u_Filter.g));
    color += texture2D(u_Texture, vec2(v_Coordinate.x-u_Filter.b, v_Coordinate.y-u_Filter.b));
    color += texture2D(u_Texture, vec2(v_Coordinate.x-u_Filter.b, v_Coordinate.y+u_Filter.b));
    color += texture2D(u_Texture, vec2(v_Coordinate.x+u_Filter.b, v_Coordinate.y-u_Filter.b));
    color += texture2D(u_Texture, vec2(v_Coordinate.x+u_Filter.b, v_Coordinate.y+u_Filter.b));
    color /= 12.0;
    gl_FragColor = color;
}
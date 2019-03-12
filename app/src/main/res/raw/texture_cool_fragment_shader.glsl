precision mediump float;

uniform sampler2D u_Texture;
uniform vec3 u_Filter;
varying vec2 v_Coordinate;

void modifyColor(vec4 color){
    color.r = max(min(color.r, 1.0), 0.0);
    color.g = max(min(color.g, 1.0), 0.0);
    color.b = max(min(color.b, 1.0), 0.0);
    color.a = max(min(color.a, 1.0), 0.0);
}

void main()
{
    vec4 color = texture2D(u_Texture, v_Coordinate);
    vec4 cool = color + vec4(u_Filter, 0.0);
    modifyColor(cool);
    gl_FragColor = cool;
}
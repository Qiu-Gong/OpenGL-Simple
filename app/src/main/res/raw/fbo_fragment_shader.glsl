precision mediump float;

uniform sampler2D u_Texture;
varying vec2 v_Coordinate;

void main()
{
    vec4 rgb = texture2D(u_Texture, v_Coordinate);
    float green = rgb.g;
    vec4 color = vec4(green, green, green, rgb.a);
    gl_FragColor = color;
}
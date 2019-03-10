attribute vec4 a_Position;

uniform mat4 u_Matrix;

varying vec4 v_Color;
attribute vec4 a_Color;

void main()
{
    gl_Position = u_Matrix * a_Position;
    v_Color = a_Color;
}
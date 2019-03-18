attribute vec4 a_Position;
attribute vec2 a_Coordinate;

uniform mat4 u_Matrix;

varying vec2 v_Coordinate;

void main()
{
    gl_Position = u_Matrix * a_Position;
    v_Coordinate = a_Coordinate;
}

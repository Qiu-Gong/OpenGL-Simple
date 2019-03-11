attribute vec4 a_Position;

uniform mat4 u_Matrix;

varying vec4 v_Color;

void main()
{
    gl_Position = u_Matrix * a_Position;
    if(a_Position.z == 0.0){
        v_Color = vec4(1.0, 1.0, 1.0, 1.0);
    }else{
        v_Color = vec4(0.0, 0.0, 0.0, 1.0);
    }
}
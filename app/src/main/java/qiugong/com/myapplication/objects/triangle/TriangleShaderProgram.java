package qiugong.com.myapplication.objects.triangle;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.programs.ShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class TriangleShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";

    private final int aPositionLocation;
    private final int uColorLocation;

    public TriangleShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorLocation() {
        return uColorLocation;
    }

    @Override
    public void setUniforms(float[] matrix, int textureId) {
    }
}


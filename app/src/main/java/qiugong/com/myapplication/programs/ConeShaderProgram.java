package qiugong.com.myapplication.programs;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;

/**
 * @author qzx 2019/3/10.
 */
public class ConeShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private final int aPositionLocation;
    private final int uMatrixLocation;

    public ConeShaderProgram(Context context) {
        super(context, R.raw.cone_vertex_shader, R.raw.cone_fragment_shader);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    @Override
    public void setUniforms(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }
}

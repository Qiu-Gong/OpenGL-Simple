package qiugong.com.myapplication.objects.cube;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.objects.ShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class CubeShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_COLOR = "a_Color";

    private final int aPositionLocation;
    private final int uMatrixLocation;
    private final int aColorLocation;

    public CubeShaderProgram(Context context) {
        super(context, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    @Override
    public void setUniforms(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }
}

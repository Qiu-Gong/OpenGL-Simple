package qiugong.com.myapplication.objects.square;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.objects.ShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class SquareShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_COLOR = "u_Color";

    private final int aPositionLocation;
    private final int uMatrixLocation;
    private final int uColorLocation;

    public SquareShaderProgram(Context context) {
        super(context, R.raw.square_vertex_shader, R.raw.square_fragment_shader);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
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
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }
}

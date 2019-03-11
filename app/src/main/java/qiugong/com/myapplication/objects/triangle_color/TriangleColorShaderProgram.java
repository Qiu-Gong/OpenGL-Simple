package qiugong.com.myapplication.objects.triangle_color;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.programs.ShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class TriangleColorShaderProgram extends ShaderProgram {
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_COLOR = "a_Color";

    private final int aPositionLocation;
    private final int uMatrixLocation;
    private final int aColorLocation;

    public TriangleColorShaderProgram(Context context) {
        super(context, R.raw.triangle_color_vertex_shader, R.raw.triangle_color_fragment_shader);

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

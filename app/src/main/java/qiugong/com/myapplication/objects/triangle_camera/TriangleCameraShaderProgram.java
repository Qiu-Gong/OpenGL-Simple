package qiugong.com.myapplication.objects.triangle_camera;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.programs.ShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class TriangleCameraShaderProgram extends ShaderProgram {
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";
    private static final String U_MATRIX = "u_Matrix";

    private final int aPositionLocation;
    private final int uColorLocation;
    private final int uMatrixLocation;

    public TriangleCameraShaderProgram(Context context) {
        super(context, R.raw.triangle_camera_vertex_shader, R.raw.triangle_camera_fragment_shader);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
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

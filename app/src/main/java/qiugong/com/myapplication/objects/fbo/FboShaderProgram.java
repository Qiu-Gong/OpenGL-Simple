package qiugong.com.myapplication.objects.fbo;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.objects.ShaderProgram;

/**
 * @author qzx 2019/3/14.
 */
public class FboShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String A_COORDINATES = "a_Coordinate";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_TEXTURE = "u_Texture";

    private final int uMatrixLocation;
    private final int uTextureLocation;

    private final int aPositionLocation;
    private final int aCoordinatesLocation;

    public FboShaderProgram(Context context) {
        super(context, R.raw.fbo_vertex_shader, R.raw.fbo_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureLocation = GLES20.glGetUniformLocation(program, U_TEXTURE);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aCoordinatesLocation = GLES20.glGetAttribLocation(program, A_COORDINATES);
    }

    @Override
    public void setUniforms(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uTextureLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getCoordinatesAttributeLocation() {
        return aCoordinatesLocation;
    }
}

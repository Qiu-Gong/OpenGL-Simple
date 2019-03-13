package qiugong.com.myapplication.camera;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.objects.ShaderProgram;

/**
 * @author qzx 2019/3/11.
 */
public class CameraTextureShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String A_COORDINATES = "a_Coordinate";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_TEXTURE = "u_Texture";

    private final int uMatrixLocation;
    private final int uTextureLocation;

    private final int aPositionLocation;
    private final int aCoordinatesLocation;

    CameraTextureShaderProgram(Context context) {
        super(context, R.raw.camera_vertex_shader, R.raw.camera_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureLocation = GLES20.glGetUniformLocation(program, U_TEXTURE);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aCoordinatesLocation = GLES20.glGetAttribLocation(program, A_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glUniform1i(uTextureLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getCoordinatesAttributeLocation() {
        return aCoordinatesLocation;
    }
}

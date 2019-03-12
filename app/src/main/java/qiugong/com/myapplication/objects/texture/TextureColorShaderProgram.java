package qiugong.com.myapplication.objects.texture;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.objects.ShaderProgram;

/**
 * @author qzx 2019/3/11.
 */
public class TextureColorShaderProgram extends ShaderProgram {

    private static final String A_POSITION = "a_Position";
    private static final String A_COORDINATES = "a_Coordinate";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_TEXTURE = "u_Texture";
    private static final String U_FILTER = "u_Filter";

    private final int uMatrixLocation;
    private final int uTextureLocation;
    private final int uFilterLocation;

    private final int aPositionLocation;
    private final int aCoordinatesLocation;

    public TextureColorShaderProgram(Context context, int fragmentShaderResourceId) {
        super(context, R.raw.texture_vertex_shader, fragmentShaderResourceId);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureLocation = GLES20.glGetUniformLocation(program, U_TEXTURE);
        uFilterLocation = GLES20.glGetUniformLocation(program, U_FILTER);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aCoordinatesLocation = GLES20.glGetAttribLocation(program, A_COORDINATES);
    }

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

    public int getFilterUniformLocation() {
        return uFilterLocation;
    }
}

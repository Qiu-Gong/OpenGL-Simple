package qiugong.com.myapplication.objects.fbo;

import android.opengl.GLES20;
import android.opengl.Matrix;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.objects.Objects;

/**
 * @author qzx 2019/3/14.
 */
public class Fbo extends Objects<FboShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int POSITION_STRIDE = POSITION_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;
    private static final int TEXTURE_STRIDE = TEXTURE_COORDINATES_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;

    private final float[] VERTEX_DATA = {
            -1.0f, 1.0f,    //左上角
            -1.0f, -1.0f,   //左下角
            1.0f, 1.0f,     //右上角
            1.0f, -1.0f     //右下角
    };

    private final float[] TEXTURE_DATA = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private final VertexArray vertexArray;
    private final VertexArray textureArray;

    public Fbo() {
        vertexArray = new VertexArray(VERTEX_DATA);
        textureArray = new VertexArray(TEXTURE_DATA);
    }

    @Override
    public void bindData(FboShaderProgram program) {
        vertexArray.setVertexAttribPointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                POSITION_STRIDE);

        textureArray.setVertexAttribPointer(
                0,
                program.getCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                TEXTURE_STRIDE);
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void setMvpMatrix(float[] mvp, float picRatio, int width, int height) {
        float[] project = new float[16];
        float[] view = new float[16];

        float ratio = (float) width / height;
        if (width > height) {
            if (picRatio > ratio) {
                Matrix.orthoM(project, 0, -ratio * picRatio, ratio * picRatio, -1, 1, 3, 7);
            } else {
                Matrix.orthoM(project, 0, -ratio / picRatio, ratio / picRatio, -1, 1, 3, 7);
            }
        } else {
            if (picRatio > ratio) {
                Matrix.orthoM(project, 0, -1, 1, -1 / ratio * picRatio, 1 / ratio * picRatio, 3, 7);
            } else {
                Matrix.orthoM(project, 0, -1, 1, -picRatio / ratio, picRatio / ratio, 3, 7);
            }
        }

        Matrix.setLookAtM(view, 0,
                0, 0, 7.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mvp, 0, project, 0, view, 0);
    }
}

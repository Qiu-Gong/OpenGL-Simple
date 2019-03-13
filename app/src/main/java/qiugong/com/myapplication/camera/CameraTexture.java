package qiugong.com.myapplication.camera;

import android.opengl.GLES20;
import android.opengl.Matrix;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.objects.Objects;
import qiugong.com.myapplication.util.MatrixHelper;

/**
 * @author qzx 2019/3/11.
 */
public class CameraTexture extends Objects<CameraTextureShaderProgram> {

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

    CameraTexture() {
        vertexArray = new VertexArray(VERTEX_DATA);
        textureArray = new VertexArray(TEXTURE_DATA);
    }

    @Override
    public void bindData(CameraTextureShaderProgram program) {
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

    public void setMvpMatrix(MatrixHelper matrixHelper, int width, int height, int previewWidth, int previewHeight) {
        if (previewHeight > 0 && previewWidth > 0 && width > 0 && height > 0) {
            float scale = (float) width / height;
            float preScale = (float) previewWidth / previewHeight;

            if (preScale > scale) {
                matrixHelper.ortho(-scale / preScale, scale / preScale, -1, 1, 1, 3);
            } else {
                matrixHelper.ortho(-1, 1, -preScale / scale, preScale / scale, 1, 3);
            }
            matrixHelper.setLookAtM(0, 0, 1, 0, 0, 0, 0, 1, 0);
        }
    }

    public void setMvpMatrix(float[] matrix, int width, int height, int previewWidth, int previewHeight) {
        if (previewHeight > 0 && previewWidth > 0 && width > 0 && height > 0) {
            float scale = (float) width / height;
            float preScale = (float) previewWidth / previewHeight;
            float[] projection = new float[16];
            float[] camera = new float[16];

            if (preScale > scale) {
                Matrix.orthoM(projection, 0, -scale / preScale, scale / preScale, -1, 1, 1, 3);
            } else {
                Matrix.orthoM(projection, 0, -1, 1, -preScale / scale, preScale / scale, 1, 3);
            }
            Matrix.setLookAtM(camera, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0);
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0);
        }
    }
}

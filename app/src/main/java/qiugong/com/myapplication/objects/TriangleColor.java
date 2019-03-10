package qiugong.com.myapplication.objects;

import android.opengl.GLES20;
import android.opengl.Matrix;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.programs.TriangleColorShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class TriangleColor extends Objects<TriangleColorShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int POSITION_STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final int COLOR_STRIDE = (COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // X, Y
            0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f
    };

    private static final float COLOR[] = {
            // R, G, B, A
            0.0f, 1.0f, 0.0f, 1.0f ,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    private final VertexArray vertexArray;
    private final VertexArray colorArray;

    public TriangleColor() {
        vertexArray = new VertexArray(VERTEX_DATA);
        colorArray = new VertexArray(COLOR);
    }

    @Override
    public void bindData(TriangleColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                POSITION_STRIDE
        );

        colorArray.setVertexAttribPointer(
                0,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                COLOR_STRIDE
        );
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_DATA.length / POSITION_COMPONENT_COUNT);
    }

    public void setMvpMatrix(float[] mvp, int width, int height) {
        float[] project = new float[16];
        float[] view = new float[16];

        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(project, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(view, 0,
                0, 0, 7.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mvp, 0, project, 0, view, 0);
    }
}

package qiugong.com.myapplication.objects;

import android.opengl.GLES20;
import android.opengl.Matrix;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.IndexArray;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.programs.CubeShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class Cube extends Objects<CubeShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int POSITION_STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final int COLOR_COMPONENT_COUNT = 4;
    private static final int COLOR_STRIDE = (COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // X, Y, Z
            -1.0f, 1.0f, 1.0f,    //正面左上0
            -1.0f, -1.0f, 1.0f,   //正面左下1
            1.0f, -1.0f, 1.0f,    //正面右下2
            1.0f, 1.0f, 1.0f,     //正面右上3
            -1.0f, 1.0f, -1.0f,   //反面左上4
            -1.0f, -1.0f, -1.0f,  //反面左下5
            1.0f, -1.0f, -1.0f,   //反面右下6
            1.0f, 1.0f, -1.0f,    //反面右上7
    };

    private static final float COLOR[] = {
            // R, G, B, A
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };

    private static final short[] INDEX = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };

    private final VertexArray vertexArray;
    private final VertexArray colorArray;
    private final IndexArray indexArray;

    public Cube() {
        vertexArray = new VertexArray(VERTEX_DATA);
        colorArray = new VertexArray(COLOR);
        indexArray = new IndexArray(INDEX);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void bindData(CubeShaderProgram colorProgram) {
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

        indexArray.setIndexPosition(0);
    }

    @Override
    public void draw() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX.length, GLES20.GL_UNSIGNED_SHORT, indexArray.getIndexBuffer());
    }

    @Override
    public void setMvpMatrix(float[] mvp, int width, int height) {
        float[] project = new float[16];
        float[] view = new float[16];

        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(project, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(view, 0,
                5.0f, 5.0f, 10.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mvp, 0, project, 0, view, 0);
    }
}

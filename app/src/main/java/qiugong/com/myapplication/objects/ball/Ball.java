package qiugong.com.myapplication.objects.ball;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.ArrayList;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.objects.Objects;

/**
 * @author qzx 2019/3/11.
 */
public class Ball extends Objects<BallShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private final VertexArray vertexArray;
    private final float[] vertexData;

    public Ball() {
        vertexData = createPositions();
        vertexArray = new VertexArray(vertexData);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;

        float step = 1f;
        for (float i = -90; i < 90 + step; i += step) {
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float) Math.sin(i * Math.PI / 180.0);
            h2 = (float) Math.sin((i + step) * Math.PI / 180.0);

            // 固定纬度, 360 度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0.0f; j < 360.0f + step; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }

        float[] array = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            array[i] = data.get(i);
        }

        return array;
    }

    @Override
    public void bindData(BallShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexData.length / POSITION_COMPONENT_COUNT);
    }

    public void setMvpMatrix(float[] mvp, int width, int height) {
        float[] project = new float[16];
        float[] view = new float[16];

        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(project, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(view, 0,
                1.0f, -10.0f, -4.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mvp, 0, project, 0, view, 0);
    }
}
package qiugong.com.myapplication.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.ArrayList;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.programs.ConeShaderProgram;
import qiugong.com.myapplication.programs.OvalShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class Cylinder extends Objects<ConeShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float RADIUS = 1.0f;
    private static final int CUT_COUNT = 360;

    private final float[] vertexData;
    private final VertexArray vertexArray;

    private final Oval top, bottom;
    private final OvalShaderProgram ovalShaderProgram;
    private float[] ovalMvpMatrix;

    public Cylinder(Context context) {
        top = new Oval(2.0f);
        bottom = new Oval(0.0f);
        ovalShaderProgram = new OvalShaderProgram(context);

        vertexData = createPositions();
        vertexArray = new VertexArray(vertexData);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        float angSpan = 360f / CUT_COUNT;
        for (float i = 0; i < 360 + angSpan; i += angSpan) {
            data.add((float) (RADIUS * Math.sin(i * Math.PI / 180f)));
            data.add((float) (RADIUS * Math.cos(i * Math.PI / 180f)));
            data.add(2.0f);
            data.add((float) (RADIUS * Math.sin(i * Math.PI / 180f)));
            data.add((float) (RADIUS * Math.cos(i * Math.PI / 180f)));
            data.add(0.0f);
        }

        float[] array = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            array[i] = data.get(i);
        }

        return array;
    }

    @Override
    public void bindData(ConeShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexData.length / POSITION_COMPONENT_COUNT);

        ovalShaderProgram.useProgram();
        ovalShaderProgram.setUniforms(ovalMvpMatrix, 0);
        top.bindData(ovalShaderProgram);
        top.draw();
        bottom.bindData(ovalShaderProgram);
        bottom.draw();
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
                1.0f, -10.0f, -4.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mvp, 0, project, 0, view, 0);

        ovalMvpMatrix = mvp;
    }
}

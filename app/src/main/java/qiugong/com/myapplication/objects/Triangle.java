package qiugong.com.myapplication.objects;

import android.opengl.GLES20;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.programs.TriangleShaderProgram;

/**
 * @author qzx 2019/3/10.
 */
public class Triangle extends Objects<TriangleShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // X, Y
            0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f
    };

    private static final float COLOR[] = {
            // R, G, B, A
            1.0f, 1.0f, 1.0f, 1.0f
    };

    private final VertexArray vertexArray;

    public Triangle() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    @Override
    public void bindData(TriangleShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        GLES20.glUniform4fv(colorProgram.getColorLocation(), 1, COLOR, 0);
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_DATA.length / POSITION_COMPONENT_COUNT);
    }
}

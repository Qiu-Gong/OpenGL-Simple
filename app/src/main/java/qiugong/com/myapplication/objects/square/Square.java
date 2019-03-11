package qiugong.com.myapplication.objects.square;

import android.opengl.GLES20;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.IndexArray;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.objects.Objects;

/**
 * @author qzx 2019/3/10.
 */
public class Square extends Objects<SquareShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // X, Y
            -0.5f, 0.5f,  // top left
            -0.5f, -0.5f, // bottom left
            0.5f, -0.5f,  // bottom right
            0.5f, 0.5f    // top right
    };

    private static final short[] INDEX = {
            0, 1, 2, 0, 2, 3
    };

    private static final float COLOR[] = {
            // R, G, B, A
            1.0f, 1.0f, 1.0f, 1.0f
    };

    private final VertexArray vertexArray;
    private final IndexArray indexArray;

    public Square() {
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = new IndexArray(INDEX);
    }

    @Override
    public void bindData(SquareShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        indexArray.setIndexPosition(0);

        GLES20.glUniform4fv(colorProgram.getColorLocation(), 1, COLOR, 0);
    }

    @Override
    public void draw() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX.length, GLES20.GL_UNSIGNED_SHORT, indexArray.getIndexBuffer());
    }
}

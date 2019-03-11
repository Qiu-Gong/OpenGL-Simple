package qiugong.com.myapplication.objects.oval;

import android.opengl.GLES20;

import java.util.ArrayList;

import qiugong.com.myapplication.Constants;
import qiugong.com.myapplication.data.VertexArray;
import qiugong.com.myapplication.objects.Objects;

/**
 * @author qzx 2019/3/10.
 */
public class Oval extends Objects<OvalShaderProgram> {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float RADIUS = 1f;
    private static final int CUT_COUNT = 360;

    private static final float COLOR[] = {
            // R, G, B, A
            1.0f, 0.0f, 0.0f, 1.0f
    };

    private final VertexArray vertexArray;
    private final float[] vertexData;
    private float height;

    public Oval(){
        this(0.0f);
    }

    public Oval(float height) {
        this.height = height;
        vertexData = createPositions();
        vertexArray = new VertexArray(vertexData);
    }

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);
        data.add(0.0f);
        data.add(height);

        float angSpan = 360f / CUT_COUNT;
        for (float i = 0; i < 360 + angSpan; i += angSpan) {
            data.add((float) (RADIUS * Math.sin(i * Math.PI / 180f)));
            data.add((float) (RADIUS * Math.cos(i * Math.PI / 180f)));
            data.add(height);
        }

        float[] array = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            array[i] = data.get(i);
        }

        return array;
    }

    @Override
    public void bindData(OvalShaderProgram colorProgram) {
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexData.length / POSITION_COMPONENT_COUNT);
    }
}

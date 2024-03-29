package qiugong.com.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import qiugong.com.myapplication.objects.cube.Cube;
import qiugong.com.myapplication.objects.cube.CubeShaderProgram;
import qiugong.com.myapplication.util.MatrixHelper;

/**
 * @author qzx 2018/11/2.
 */
class MatrixRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private Cube object;
    private CubeShaderProgram shaderProgram;
    private MatrixHelper helper;

    MatrixRenderer(Context context) {
        this.context = context;
        helper = new MatrixHelper();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);

        object = new Cube();
        shaderProgram = new CubeShaderProgram(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float rate = width / (float) height;
        helper.frustum(-rate * 3, rate * 3, -3, 3, 3, 20);
        helper.setLookAtM(0, 0, 10, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        shaderProgram.useProgram();
        shaderProgram.setUniforms(helper.getMvpMatrix(), 0);
        object.bindData(shaderProgram);
        object.draw();

        helper.pushStack();
        helper.translate(0, 3, 0);
        shaderProgram.setUniforms(helper.getMvpMatrix(), 0);
        object.draw();
        helper.popStack();

        helper.pushStack();
        helper.translate(0, -3, 0);
        helper.rotate(30f, 1, 1, 1);
        shaderProgram.setUniforms(helper.getMvpMatrix(), 0);
        object.draw();
        helper.popStack();

        helper.pushStack();
        helper.translate(-3, 0, 0);
        helper.scale(0.5f, 0.5f, 0.5f);

        helper.pushStack();
        helper.translate(12, 0, 0);
        helper.scale(1.0f, 2.0f, 1.0f);
        helper.rotate(30f, 1, 2, 1);
        shaderProgram.setUniforms(helper.getMvpMatrix(), 0);
        object.draw();
        helper.popStack();

        helper.rotate(30f, -1, -1, 1);
        shaderProgram.setUniforms(helper.getMvpMatrix(), 0);
        object.draw();
        helper.popStack();
    }
}

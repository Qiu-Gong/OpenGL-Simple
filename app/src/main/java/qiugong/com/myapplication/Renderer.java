package qiugong.com.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import qiugong.com.myapplication.objects.ball.Ball;
import qiugong.com.myapplication.objects.ball.BallShaderProgram;

/**
 * @author qzx 2018/11/2.
 */
class Renderer implements GLSurfaceView.Renderer {

    private final Context context;

    private float[] mvpMatrix = new float[16];

    private Ball object;
    private BallShaderProgram shaderProgram;

    Renderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        object = new Ball();
        shaderProgram = new BallShaderProgram(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        object.setMvpMatrix(mvpMatrix, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        shaderProgram.useProgram();
        shaderProgram.setUniforms(mvpMatrix, 0);
        object.bindData(shaderProgram);
        object.draw();
    }
}

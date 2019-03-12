package qiugong.com.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import qiugong.com.myapplication.objects.cube.Cube;
import qiugong.com.myapplication.objects.cube.CubeShaderProgram;

/**
 * @author qzx 2018/11/2.
 */
class MatrixRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private float[] mvpMatrix = new float[16];

    private Cube object;
    private CubeShaderProgram shaderProgram;
    private int width, height;

    MatrixRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);

        object = new Cube();
        shaderProgram = new CubeShaderProgram(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
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

    public void onChangeListener(float left, float right, float bottom, float top, float near, float far,
                                 float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        object.setMvpMatrix(mvpMatrix, width, height, left, right, bottom, top, near, far,
                eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }
}

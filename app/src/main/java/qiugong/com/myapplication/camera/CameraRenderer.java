package qiugong.com.myapplication.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import qiugong.com.myapplication.util.MatrixHelper;
import qiugong.com.myapplication.util.TextureHelper;

/**
 * @author qzx 2018/11/2.
 */
public class CameraRenderer implements GLSurfaceView.Renderer,
        SurfaceTexture.OnFrameAvailableListener {

    private final Context context;

    private CameraImpl camera;
    private SurfaceTexture surfaceTexture;
    private GLSurfaceView glSurfaceView;

    private CameraTexture object;
    private CameraTextureShaderProgram shaderProgram;
    private final MatrixHelper matrixHelper;

    private int textureID;

    public CameraRenderer(Context context, GLSurfaceView surfaceView) {
        this.context = context;
        camera = new CameraImpl();
        matrixHelper = new MatrixHelper();
        glSurfaceView = surfaceView;
    }

    public void onResume() {
        camera.openCamera(false);
        if (surfaceTexture != null) {
            camera.startPreview(surfaceTexture);
        }
    }

    public void onPause() {
        camera.closeCamera();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        object = new CameraTexture();
        shaderProgram = new CameraTextureShaderProgram(context);

        textureID = TextureHelper.createOESTexture();
        surfaceTexture = new SurfaceTexture(textureID);
        surfaceTexture.setOnFrameAvailableListener(this);
        camera.startPreview(surfaceTexture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        object.setMvpMatrix(matrixHelper, width, height, camera.getPreviewWidth(), camera.getPreviewHeight());
        if (camera.isBack()) {
            matrixHelper.rotate(270, 0, 0, 1);
        } else {
            matrixHelper.flip(true, false);
            matrixHelper.rotate(90, 0, 0, 1);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        shaderProgram.useProgram();
        shaderProgram.setUniforms(matrixHelper.getMvpMatrix(), textureID);
        object.bindData(shaderProgram);
        object.draw();

        surfaceTexture.updateTexImage();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        glSurfaceView.requestRender();
    }
}

package qiugong.com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import qiugong.com.myapplication.objects.texture.Texture;
import qiugong.com.myapplication.objects.texture.TextureShaderProgram;
import qiugong.com.myapplication.util.TextureHelper;

/**
 * @author qzx 2018/11/2.
 */
class Renderer implements GLSurfaceView.Renderer {

    private final Context context;

    private float[] mvpMatrix = new float[16];

    private Texture object;
    private TextureShaderProgram shaderProgram;
    private int texture;
    private float picRatio;

    Renderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        object = new Texture();
        shaderProgram = new TextureShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.img_texture);

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_texture, new BitmapFactory.Options());
        if (bitmap != null) {
            picRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        object.setMvpMatrix(mvpMatrix, picRatio, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        shaderProgram.useProgram();
        shaderProgram.setUniforms(mvpMatrix, texture);
        object.bindData(shaderProgram);
        object.draw();
    }
}

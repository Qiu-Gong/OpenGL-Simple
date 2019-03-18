package qiugong.com.myapplication.objects.fbo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.objects.texture.Texture;
import qiugong.com.myapplication.objects.texture.TextureShaderProgram;
import qiugong.com.myapplication.util.TextureHelper;

/**
 * @author qzx 2018/11/2.
 */
public class FboRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private float[] mvpMatrix = new float[16];

    private Texture texture;
    private TextureShaderProgram textureShaderProgram;
    private Fbo fbo;
    private FboShaderProgram fboShaderProgram;

    private int[] frame = new int[1];
    private int[] textures = new int[2]; // 第一张普通纹理，第二张绑定frame

    private Bitmap bitmap;
    private ByteBuffer buffer;
    private float imgRatio;

    private final int width = 1080, height = 2150;

    public FboRenderer(Context context) {
        this.context = context;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_texture, new BitmapFactory.Options());
        if (bitmap != null) {
            imgRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        texture = new Texture();
        textureShaderProgram = new TextureShaderProgram(context);
        textures[0] = TextureHelper.createTexture(GLES20.GL_TEXTURE_2D, bitmap, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR);

        fbo = new Fbo();
        fboShaderProgram = new FboShaderProgram(context);
        textures[1] = TextureHelper.createFrameTexture(GLES20.GL_TEXTURE_2D, width, height, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR);
        frame[0] = TextureHelper.createFrameBuffer(textures[1]);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        buffer = ByteBuffer.allocate(width * height * 4);
        buffer.order(ByteOrder.nativeOrder());

        texture.setMvpMatrix(mvpMatrix, imgRatio, width, height);
        fbo.setMvpMatrix(mvpMatrix, imgRatio, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 绑定帧缓冲区到屏幕
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glClearColor(1f, 0f, 0f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(mvpMatrix, textures[0]);
        texture.bindData(textureShaderProgram);
        texture.draw();

        // 绑定 frame[0]
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frame[0]);
        // 绘制到 frame[0]
        fboShaderProgram.useProgram();
        fboShaderProgram.setUniforms(mvpMatrix, textures[0]);
        fbo.bindData(fboShaderProgram);
        fbo.draw();

        // 读取当前帧缓冲区 frame[0]
        TextureHelper.saveTextureToSdcard(0, width, height, 1);
        TextureHelper.saveTextureToSdcard(textures[1], width, height, 2);
    }
}

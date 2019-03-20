package qiugong.com.myapplication.egl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.DisplayMetrics;

import qiugong.com.myapplication.R;
import qiugong.com.myapplication.egl.EglCore;
import qiugong.com.myapplication.egl.EglOffscreenSurface;
import qiugong.com.myapplication.objects.texture.Texture;
import qiugong.com.myapplication.objects.texture.TextureShaderProgram;
import qiugong.com.myapplication.util.TextureHelper;

public class EglActivity extends Activity {

    private Bitmap bitmap;
    private float imgRatio;
    private int width;
    private int height;

    private EglCore eglCore;
    private EglOffscreenSurface offscreenSurface;

    private Texture texture;
    private TextureShaderProgram textureShaderProgram;
    private float[] mvpMatrix = new float[16];
    private int textureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.egl_main);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;

        // 获取图片信息
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_texture, new BitmapFactory.Options());
        if (bitmap != null) {
            imgRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        }

        // 创建 EGL 环境
        eglCore = new EglCore(3);
        offscreenSurface = new EglOffscreenSurface(eglCore, width, height);
        offscreenSurface.makeCurrent();

        // 创建纹理
        texture = new Texture();
        textureShaderProgram = new TextureShaderProgram(this);
        textureId = TextureHelper.createTexture(GLES20.GL_TEXTURE_2D, bitmap, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR);

        // 绘制
        texture.setMvpMatrix(mvpMatrix, imgRatio, width, height);
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(mvpMatrix, textureId);
        texture.bindData(textureShaderProgram);
        texture.draw();
        TextureHelper.saveTextureToSdcard(0, width, height, 2);
    }
}

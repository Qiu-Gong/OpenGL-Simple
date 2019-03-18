package qiugong.com.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author qzx 2018/11/16.
 */
public class TextureHelper {

    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, int resourceId) {

        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
            }

            GLES20.glDeleteTextures(1, textureObjectIds, 0);
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        // 最小过滤 使用MIP贴图的双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        // 最大过滤 双线性
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        if (bitmap != null) {
            bitmap.recycle();
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static int createOESTexture() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    public static int createTexture(int target, Bitmap bitmap, int wrapMode, int filterMode) {
        final int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(target, texture[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_S, wrapMode);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T, wrapMode);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER, filterMode);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER, filterMode);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return texture[0];
    }

    public static int createFrameBuffer(int texture) {
        final int[] fbo = new int[1];
        GLES20.glGenFramebuffers(1, fbo, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture, 0);
        int fboStatus = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        int error = GLES20.glGetError();
        if (fboStatus != GLES20.GL_FRAMEBUFFER_COMPLETE && error != GLES20.GL_NO_ERROR) {
            throw new RuntimeException("Failed to create frame buffers: status = " + fboStatus + "; error = " + error);
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        return fbo[0];
    }

    public static int createFrameTexture(int target, int width, int height, int wrapMode, int filterMode) {
        final int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(target, texture[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_S, wrapMode);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T, wrapMode);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER, filterMode);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER, filterMode);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return texture[0];
    }

    public static void saveTextureToSdcard(int ids, int width, int height, int number) {
        ByteBuffer rgbaBuf = ByteBuffer.allocateDirect(width * height * 4);
        rgbaBuf.order(ByteOrder.nativeOrder());
        if (ids == 0) {
            // 读取显存数据
            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rgbaBuf);
        } else {
            // 读取纹理数据
            readTexture(ids, rgbaBuf, width, height);
        }

        // 保存ByteBuffer
        saveRgbToSdcard(rgbaBuf, Environment.getExternalStorageDirectory().getAbsolutePath() + "/gl_dump_Texture" + width + "_" + height + "number=" + number + ".jpg", width, height);
    }

    private static void readTexture(int ids, ByteBuffer data, int width, int height) {
        int[] offscreenFBO = new int[1];
        GLES20.glGenFramebuffers(1, offscreenFBO, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, offscreenFBO[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, ids, 0);
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
        GLES20.glDeleteFramebuffers(1, offscreenFBO, 0);
    }

    private static void saveRgbToSdcard(Buffer buf, String filename, int width, int height) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filename));
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            buf.rewind();
            buf.position(0);
            bmp.copyPixelsFromBuffer(buf);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bmp.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

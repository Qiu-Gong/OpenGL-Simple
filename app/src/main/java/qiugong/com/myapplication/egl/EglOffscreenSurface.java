package qiugong.com.myapplication.egl;

/**
 * @author qzx 2019/3/20.
 */
public class EglOffscreenSurface extends EglSurfaceBase {

    public EglOffscreenSurface(EglCore eglCore, int width, int height) {
        super(eglCore);
        mEGLSurface = mEglCore.createOffscreenSurface(width, height);
        mWidth = width;
        mHeight = height;
    }
}

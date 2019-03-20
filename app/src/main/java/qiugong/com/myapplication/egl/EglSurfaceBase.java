package qiugong.com.myapplication.egl;

import android.opengl.EGL14;
import android.opengl.EGLSurface;

/**
 * @author qzx 2019/3/20.
 */
public abstract class EglSurfaceBase {

    protected static final int INVALID = -1;

    protected EglCore mEglCore;
    protected EGLSurface mEGLSurface;
    protected int mWidth = INVALID;
    protected int mHeight = INVALID;

    public EglSurfaceBase(EglCore eglCore) {
        mEglCore = eglCore;
    }

    public boolean release() {
        if (mEglCore == null || mEGLSurface == null) {
            return false;
        }

        boolean result = mEglCore.destroySurface(mEGLSurface);
        mEglCore = null;
        mEGLSurface = null;
        mWidth = INVALID;
        mHeight = INVALID;
        return result;
    }

    public EglCore getEglCore() {
        return mEglCore;
    }

    public EGLSurface getEGLSurface() {
        return mEGLSurface;
    }

    public int getWidth() {
        if (mWidth == INVALID && mEglCore != null) {
            return mEglCore.querySurface(mEGLSurface, EGL14.EGL_WIDTH);
        } else {
            return mWidth;
        }
    }

    public int getHeight() {
        if (mHeight == INVALID && mEglCore != null) {
            return mEglCore.querySurface(mEGLSurface, EGL14.EGL_HEIGHT);
        } else {
            return mHeight;
        }
    }

    public void makeCurrent() {
        if (!mEglCore.isCurrent(mEGLSurface)) {
            mEglCore.makeCurrent(mEGLSurface);
        }
    }

    public boolean swapBuffers() {
        return mEglCore.swapBuffers(mEGLSurface);
    }

    @Override
    public String toString() {
        return "EglSurfaceInfo: eglCore =" + mEglCore + "; eglSurface = " + mEGLSurface + "; width = " + mWidth + "; height =" + mHeight;
    }
}

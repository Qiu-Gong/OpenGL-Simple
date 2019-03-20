package qiugong.com.myapplication.egl;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.view.Surface;

/**
 * @author qzx 2019/3/20.
 */
public class EglCore {

    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLConfig mEGLConfig;

    public EglCore(@IntRange(from = 2, to = 3) int version) {
        this(version, EGL14.EGL_NO_CONTEXT);
    }

    public EglCore(EGLContext sharedContext) {
        this(3, sharedContext);
    }

    public EglCore(@IntRange(from = 2, to = 3) int version, EGLContext sharedContext) {
        if (sharedContext == null) {
            sharedContext = EGL14.EGL_NO_CONTEXT;
        }

        // 获取 Display
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY || EGL14.eglGetError() != EGL14.EGL_SUCCESS) {
            throw new RuntimeException("Failed to get EGLDisplay.");
        }

        // 初始化
        int[] majorVersion = new int[1];
        int[] minorVersion = new int[1];
        if (!EGL14.eglInitialize(mEGLDisplay, majorVersion, 0, minorVersion, 0)) {
            throw new RuntimeException("Failed to initialize EGL.");
        }

        // 选择配置
        int renderableType = version >= 3 ? EGL14.EGL_OPENGL_ES2_BIT | EGLExt.EGL_OPENGL_ES3_BIT_KHR : EGL14.EGL_OPENGL_ES2_BIT;
        int[] configAttrs = new int[]{
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, renderableType,
                EGL14.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        if (!EGL14.eglChooseConfig(mEGLDisplay, configAttrs, 0, configs, 0, configs.length, numConfig, 0)) {
            throw new RuntimeException("Failed to choose EGLConfig.");
        }
        mEGLConfig = configs[0];

        // 获取 context
        int[] contextAttrs = new int[]{EGL14.EGL_CONTEXT_CLIENT_VERSION, version, EGL14.EGL_NONE};
        mEGLContext = EGL14.eglCreateContext(mEGLDisplay, mEGLConfig, sharedContext, contextAttrs, 0);
        if (EGL14.eglGetError() != EGL14.EGL_SUCCESS || mEGLContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("Failed to create EGLContext.");
        }
    }

    public void makeCurrent(EGLSurface eglSurface) {
        makeCurrent(eglSurface, eglSurface);
    }

    public void makeCurrent(EGLSurface drawSurface, EGLSurface readSurface) {
        if (!EGL14.eglMakeCurrent(mEGLDisplay, drawSurface, readSurface, mEGLContext)) {
            throw new RuntimeException("Failed to make current.");
        }
    }

    public void clearCurrent() {
        if (!EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            throw new RuntimeException("Failed to release current.");
        }
    }

    private void checkEglSurface(EGLSurface eglSurface) {
        if (eglSurface == EGL14.EGL_NO_SURFACE || EGL14.eglGetError() != EGL14.EGL_SUCCESS) {
            throw new RuntimeException("Failed to create window surface.");
        }
    }

    public EGLSurface createOffscreenSurface(@IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        int[] attrs = new int[]{EGL14.EGL_WIDTH, width, EGL14.EGL_HEIGHT, height, EGL14.EGL_NONE};
        EGLSurface eglSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, mEGLConfig, attrs, 0);
        checkEglSurface(eglSurface);
        return eglSurface;
    }

    public EGLSurface createWindowSurface(Surface surface) {
        return createWindowSurfaceInner(surface);
    }

    public EGLSurface createWindowSurface(SurfaceTexture surfaceTexture) {
        return createWindowSurfaceInner(surfaceTexture);
    }

    private EGLSurface createWindowSurfaceInner(Object window) {
        int[] attrs = new int[]{EGL14.EGL_NONE};
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, window, attrs, 0);
        checkEglSurface(eglSurface);
        return eglSurface;
    }

    public boolean destroySurface(EGLSurface surface) {
        return EGL14.eglDestroySurface(mEGLDisplay, surface);
    }

    @IntDef({
            EGL14.EGL_CONFIG_ID,
            EGL14.EGL_WIDTH,
            EGL14.EGL_HEIGHT,
            EGL14.EGL_HORIZONTAL_RESOLUTION,
            EGL14.EGL_LARGEST_PBUFFER,
            EGL14.EGL_MIPMAP_LEVEL,
            EGL14.EGL_MIPMAP_TEXTURE,
            EGL14.EGL_MULTISAMPLE_RESOLVE,
            EGL14.EGL_PIXEL_ASPECT_RATIO,
            EGL14.EGL_RENDER_BUFFER,
            EGL14.EGL_SWAP_BEHAVIOR,
            EGL14.EGL_TEXTURE_FORMAT,
            EGL14.EGL_TEXTURE_TARGET,
            EGL14.EGL_VERTICAL_RESOLUTION,
    })
    private @interface SurfaceQuery {
    }

    public int querySurface(EGLSurface eglSurface, @SurfaceQuery int what) {
        int[] value = new int[1];
        EGL14.eglQuerySurface(mEGLDisplay, eglSurface, what, value, 0);
        return value[0];
    }

    public boolean swapBuffers(EGLSurface eglSurface) {
        return EGL14.eglSwapBuffers(mEGLDisplay, eglSurface);
    }

    public boolean isCurrent(EGLSurface eglSurface) {
        return isCurrent(eglSurface, eglSurface);
    }

    public boolean isCurrent(EGLSurface drawSurface, EGLSurface readSurface) {
        return mEGLContext.equals(EGL14.eglGetCurrentContext()) &&
                drawSurface.equals(EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW)) &&
                readSurface.equals(EGL14.eglGetCurrentSurface(EGL14.EGL_READ));
    }

    public void release() {
        EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEGLDisplay);
        mEGLDisplay = null;
        mEGLContext = null;
        mEGLConfig = null;
    }

    @Override
    public String toString() {
        EGLDisplay display = EGL14.eglGetCurrentDisplay();
        EGLContext context = EGL14.eglGetCurrentContext();
        EGLSurface surface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW);
        return "Current EGL: display = " + display + "; context = " + context + "; surface = " + surface;
    }
}

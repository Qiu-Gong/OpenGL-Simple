package qiugong.com.myapplication.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 相机
 *
 * @author qzx 2018/2/5.
 */
public class CameraImpl {

    private static final String TAG = "CameraImpl";

    private static final int TYPE_PREVIEW = 1;
    private static final int TYPE_PICTURE = 2;

    private static final int TYPE_SCALE_16_9 = 1;
    private static final int TYPE_SCALE_4_3 = 2;
    private static final int TYPE_SCALE_1_1 = 3;

    private Camera mCamera;
    private int mCameraId = 1;
    private int mPreviewWidth, mPreviewHeight;
    private Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();

    /**
     * 开启相机
     *
     * @param isBack true 后置  false 前置
     */
    void openCamera(boolean isBack) {
        if (isBack) {
            mCameraId = 0;
        } else {
            mCameraId = 1;
        }

        mCamera = Camera.open(mCameraId);
        Camera.getCameraInfo(mCameraId, mCameraInfo);
        setBestResolution(mCamera, TYPE_PREVIEW, TYPE_SCALE_16_9);
        setBestResolution(mCamera, TYPE_PICTURE, TYPE_SCALE_16_9);
    }

    /**
     * 关闭相机
     */
    void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始预览
     *
     * @param surfaceTexture surfaceTexture
     */
    void startPreview(SurfaceTexture surfaceTexture) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surfaceTexture);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置最佳尺寸
     *
     * @param camera 摄像头
     * @param type   设置类型
     * @param scale  比例尺寸
     */
    private void setBestResolution(Camera camera, int type, int scale) {
        Camera.Parameters p = camera.getParameters();
        List<Camera.Size> supportedSize = null;
        if (type == TYPE_PICTURE) {
            supportedSize = p.getSupportedPictureSizes();
            Log.d(TAG, "picture default resolution " + p.getPictureSize().width + "x" + p.getPictureSize().height);
        } else if (type == TYPE_PREVIEW) {
            supportedSize = p.getSupportedPreviewSizes();
            Log.d(TAG, "preview resolution " + p.getPreviewSize().width + "x" + p.getPreviewSize().height);
        }

        if (supportedSize == null || supportedSize.size() == 0) {
            return;
        }

        // 比例筛选
        List<Camera.Size> sortedResolutions = new ArrayList<>();
        for (Camera.Size size : supportedSize) {
            if (scale == TYPE_SCALE_16_9) {
                if ((float) size.width / (float) size.height == 16f / 9f) {
                    sortedResolutions.add(size);
                }
            } else if (scale == TYPE_SCALE_4_3) {
                if ((float) size.width / (float) size.height == 4f / 3f) {
                    sortedResolutions.add(size);
                }
            } else if (scale == TYPE_SCALE_1_1) {
                if ((float) size.width / (float) size.height == 1f / 1f) {
                    sortedResolutions.add(size);
                }
            }
        }

        Collections.sort(sortedResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                if (a.width > b.width) {
                    return -1;
                } else if (a.width < b.width) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        if (sortedResolutions.size() > 0) {
            if (type == TYPE_PICTURE) {
                p.setPictureSize(sortedResolutions.get(0).width, sortedResolutions.get(0).height);
            } else if (type == TYPE_PREVIEW) {
                mPreviewWidth = sortedResolutions.get(0).width;
                mPreviewHeight = sortedResolutions.get(0).height;
                p.setPreviewSize(sortedResolutions.get(0).width, sortedResolutions.get(0).height);
            }
            camera.setParameters(p);
        }
    }

    int getPreviewWidth() {
        return mPreviewWidth;
    }

    int getPreviewHeight() {
        return mPreviewHeight;
    }

    boolean isBack() {
        if (mCameraId == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Camera.CameraInfo getCameraInfo() {
        return mCameraInfo;
    }
}

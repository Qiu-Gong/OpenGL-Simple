package qiugong.com.myapplication.util;

import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author qzx 2018/11/12.
 */
public class MatrixHelper {

    private float[] project = new float[16];
    private float[] view = new float[16];
    private float[] mode = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,};

    private Stack<float[]> stack = new Stack<>();

    public void pushStack() {
        stack.push(Arrays.copyOf(mode, 16));
    }

    public void popStack() {
        mode = stack.pop();
    }

    public void clearStack() {
        stack.clear();
    }

    //设置相机
    public void setLookAtM(float ex, float ey, float ez,
                           float cx, float cy, float cz,
                           float ux, float uy, float uz) {
        Matrix.setLookAtM(view, 0, ex, ey, ez, cx, cy, cz, ux, uy, uz);
    }

    // 透视投影
    public void frustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(project, 0, left, right, bottom, top, near, far);
    }

    // 正交投影
    public void ortho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(project, 0, left, right, bottom, top, near, far);
    }

    //平移变换
    public void translate(float x, float y, float z) {
        Matrix.translateM(mode, 0, x, y, z);
    }

    //旋转变换
    public void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mode, 0, angle, x, y, z);
    }

    // 镜像
    public void flip(boolean x, boolean y) {
        Matrix.scaleM(mode, 0, x ? -1 : 1, y ? -1 : 1, 1);
    }

    //缩放变换
    public void scale(float x, float y, float z) {
        Matrix.scaleM(mode, 0, x, y, z);
    }

    public float[] getMvpMatrix() {
        float[] temp = new float[16];
        float[] mvp = new float[16];
        Matrix.multiplyMM(temp, 0, view, 0, mode, 0);
        Matrix.multiplyMM(mvp, 0, project, 0, temp, 0);
        return mvp;
    }
}

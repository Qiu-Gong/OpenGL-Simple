package qiugong.com.myapplication.objects;

import android.content.Context;
import android.opengl.GLES20;

import qiugong.com.myapplication.util.ShaderHelper;
import qiugong.com.myapplication.util.TextResourceReader;

/**
 * @author qzx 2018/11/25.
 */
public abstract class ShaderProgram {

    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }

    public abstract void setUniforms(float[] matrix, int textureId);
}

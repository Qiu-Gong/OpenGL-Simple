package qiugong.com.myapplication.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import qiugong.com.myapplication.Constants;

/**
 * @author qzx 2018/11/17.
 */
public class IndexArray {

    private final ShortBuffer shortBuffer;

    public IndexArray(short[] indexData) {
        shortBuffer = ByteBuffer
                .allocateDirect(indexData.length * Constants.BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
    }

    public void setIndexPosition(int dataOffset) {
        shortBuffer.position(dataOffset);
    }

    public ShortBuffer getIndexBuffer(){
        return shortBuffer;
    }
}

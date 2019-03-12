package qiugong.com.myapplication;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author qzx 2019/3/12.
 */
public class SeekBarSelector extends RelativeLayout
        implements SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener {

    private TextView text;

    private SeekBarListener seekBarListener;
    private int lookId;

    private float leftValue, rightValue, bottomValue, topValue, nearValue = 3f, farValue = 20f;
    private float eyeX = 5.0f, eyeY = 5.0f, eyeZ = 10.0f, centerX = 0f, centerY = 0f, centerZ = 0f, upX = 0f, upY = 1f, upZ = 0f;

    interface SeekBarListener {
        void onChangeListener(float left, float right,
                              float bottom, float top,
                              float near, float far,
                              float eyeX, float eyeY, float eyeZ,
                              float centerX, float centerY, float centerZ,
                              float upX, float upY, float upZ);
    }

    public void setSeekBarListener(SeekBarListener seekBarListener) {
        this.seekBarListener = seekBarListener;
    }

    public SeekBarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LinearLayout.inflate(context, R.layout.seek_bar_selector, this);
        AppCompatSeekBar left = view.findViewById(R.id.left);
        AppCompatSeekBar right = view.findViewById(R.id.right);
        AppCompatSeekBar bottom = view.findViewById(R.id.bottom);
        AppCompatSeekBar top = view.findViewById(R.id.top);
        AppCompatSeekBar near = view.findViewById(R.id.near);
        AppCompatSeekBar far = view.findViewById(R.id.far);
        left.setOnSeekBarChangeListener(this);
        right.setOnSeekBarChangeListener(this);
        bottom.setOnSeekBarChangeListener(this);
        top.setOnSeekBarChangeListener(this);
        near.setOnSeekBarChangeListener(this);
        far.setOnSeekBarChangeListener(this);

        AppCompatSeekBar x = view.findViewById(R.id.x);
        x.setOnSeekBarChangeListener(this);
        AppCompatSeekBar y = view.findViewById(R.id.y);
        y.setOnSeekBarChangeListener(this);
        AppCompatSeekBar z = view.findViewById(R.id.z);
        z.setOnSeekBarChangeListener(this);

        RadioGroup look = view.findViewById(R.id.look);
        look.setOnCheckedChangeListener(this);

        text = view.findViewById(R.id.text);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int pro, boolean fromUser) {
        float progress = pro / 100f;
        switch (seekBar.getId()) {
            case R.id.left:
                text.setText("left:" + String.valueOf(progress));
                leftValue = progress;
                break;

            case R.id.right:
                text.setText("right:" + String.valueOf(progress));
                rightValue = progress;
                break;

            case R.id.bottom:
                text.setText("bottom:" + String.valueOf(progress));
                bottomValue = progress;
                break;

            case R.id.top:
                text.setText("top:" + String.valueOf(progress));
                topValue = progress;
                break;

            case R.id.near:
                nearValue = pro / 1f;
                text.setText("near:" + String.valueOf(nearValue));
                break;

            case R.id.far:
                farValue = pro / 1f;
                text.setText("far:" + String.valueOf(farValue));
                break;

            case R.id.x:
                switch (lookId) {
                    case R.id.eye:
                        eyeX = pro / 1f;
                        text.setText("eye x:" + String.valueOf(eyeX));
                        break;
                    case R.id.center:
                        centerX = pro / 1f;
                        text.setText("center x:" + String.valueOf(centerX));
                        break;
                    case R.id.up:
                        upX = pro / 1f;
                        text.setText("up x:" + String.valueOf(upX));
                        break;
                }
                break;

            case R.id.y:
                switch (lookId) {
                    case R.id.eye:
                        eyeY = pro / 1f;
                        text.setText("eye y:" + String.valueOf(eyeY));
                        break;
                    case R.id.center:
                        centerY = pro / 1f;
                        text.setText("center y:" + String.valueOf(centerY));
                        break;
                    case R.id.up:
                        upY = pro / 1f;
                        text.setText("up y:" + String.valueOf(upY));
                        break;
                }
                break;

            case R.id.z:
                switch (lookId) {
                    case R.id.eye:
                        eyeZ = pro / 1f;
                        text.setText("eye z:" + String.valueOf(eyeZ));
                        break;
                    case R.id.center:
                        centerZ = pro / 1f;
                        text.setText("center z:" + String.valueOf(centerZ));
                        break;
                    case R.id.up:
                        upZ = pro / 1f;
                        text.setText("up z:" + String.valueOf(upZ));
                        break;
                }
                break;
        }

        if (seekBarListener != null) {
            seekBarListener.onChangeListener(leftValue, rightValue, bottomValue, topValue, nearValue, farValue,
                    eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.eye:
                lookId = R.id.eye;
                break;

            case R.id.center:
                lookId = R.id.center;
                break;

            case R.id.up:
                lookId = R.id.up;
                break;
        }
    }
}

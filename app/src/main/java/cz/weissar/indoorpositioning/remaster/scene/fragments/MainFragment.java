package cz.weissar.indoorpositioning.remaster.scene.fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.weissar.indoorpositioning.IndoorPositioningApp;
import cz.weissar.indoorpositioning.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by petrw on 15.09.2017.
 */

public class MainFragment extends Fragment {

    @BindView(R.id.accelerometerXTextView)
    protected TextView accelerometerXTextView;
    @BindView(R.id.accelerometerYTextView)
    protected TextView accelerometerYTextView;
    @BindView(R.id.accelerometerZTextView)
    protected TextView accelerometerZTextView;
    @BindView(R.id.gyroscopeXTextView)
    protected TextView gyroscopeXTextView;
    @BindView(R.id.gyroscopeYTextView)
    protected TextView gyroscopeYTextView;
    @BindView(R.id.gyroscopeZTextView)
    protected TextView gyroscopeZTextView;
    @BindView(R.id.rotationXTextView)
    protected TextView rotationXTextView;
    @BindView(R.id.rotationYTextView)
    protected TextView rotationYTextView;
    @BindView(R.id.rotationZTextView)
    protected TextView rotationZTextView;

    @BindView(R.id.xPlus)
    protected FrameLayout xPlus;
    @BindView(R.id.xMinus)
    protected FrameLayout xMinus;
    @BindView(R.id.yPlus)
    protected FrameLayout yPlus;
    @BindView(R.id.yMinus)
    protected FrameLayout yMinus;
    @BindView(R.id.zPlus)
    protected FrameLayout zPlus;
    @BindView(R.id.zMinus)
    protected FrameLayout zMinus;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        Sensor linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] vec = event.values;
                if (vec[0] > 0) {
                    float x = 1.0f - Math.min(vec[0], 1.0f);
                    xPlus.setPadding(0, (int) (x * xPlus.getHeight()), 0, 0);
                    xMinus.setPadding(0, 0, 0, xMinus.getHeight());
                } else {
                    float x = -1.0f - Math.max(vec[0], -1.0f);
                    xMinus.setPadding(0, 0, 0, (int) ((-x) * xMinus.getHeight()));
                    xPlus.setPadding(0, 0, 0, xPlus.getHeight());
                }
                if (vec[1] > 0) {
                    float y = 1.0f - Math.min(vec[1], 1.0f);
                    yPlus.setPadding(0, (int) (y * yPlus.getHeight()), 0, 0);
                    yMinus.setPadding(0, 0, 0, yMinus.getHeight());
                } else {
                    float y = -1.0f - Math.max(vec[1], -1.0f);
                    yMinus.setPadding(0, 0, 0, (int) ((-y) * yMinus.getHeight()));
                    yPlus.setPadding(0, 0, 0, yPlus.getHeight());
                }
                if (vec[2] > 0) {
                    float z = 1.0f - Math.min(vec[2], 1.0f);
                    zPlus.setPadding(0, (int) (z * zPlus.getHeight()), 0, 0);
                    zMinus.setPadding(0, 0, 0, zMinus.getHeight());
                } else {
                    float z = -1.0f - Math.max(vec[2], -1.0f);
                    zMinus.setPadding(0, 0, 0, (int) ((-z) * zMinus.getHeight()));
                    zPlus.setPadding(0, 0, 0, zPlus.getHeight());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, linearAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
}

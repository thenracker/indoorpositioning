package cz.weissar.indoorpositioning.remaster.scene.fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.weissar.indoorpositioning.R;
import cz.weissar.indoorpositioning.remaster.scene.interfaces.MovementChange;
import cz.weissar.indoorpositioning.remaster.utils.VectorHolder;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by petrw on 15.09.2017.
 */

public class SensorFragment extends Fragment implements MovementChange {

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
    @BindView(R.id.wPlus)
    protected FrameLayout wPlus;
    @BindView(R.id.wMinus)
    protected FrameLayout wMinus;
    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    int sensorType;
    float maxVal;
    float recordX, recordY, recordZ;
    boolean record = false;

    private String FORMAT = "%.3f";

    private VectorHolder holder; // = new VectorHolder();
    private float[] pos = new float[3];

    public static SensorFragment newInstance(int typeLinearAcceleration, float maxVal) {
        SensorFragment frag = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt("TYPE", typeLinearAcceleration);
        args.putFloat("MAX", maxVal);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            sensorType = getArguments().getInt("TYPE");
            maxVal = getArguments().getFloat("MAX");
        } else {
            sensorType = savedInstanceState.getInt("TYPE");
            maxVal = savedInstanceState.getFloat("MAX");
        }
        holder = new VectorHolder(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("TYPE", sensorType);
        outState.putFloat("MAX", maxVal);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        holder.register((SensorManager) getContext().getSystemService(SENSOR_SERVICE));
        //initViewAndRegister();
    }

    @Override
    public void onPause() {
        holder.unregister();
        //unregister();
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onMovement(VectorHolder.Axis axis, double distance) {
        //Log.d(axis.name(), String.format(" - moved by %s metters", distance));
        switch (axis){
            case X: gyroscopeXTextView.setText(String.format("%s - moved by %s meters", axis.name(), distance)); break;
            case Y: gyroscopeYTextView.setText(String.format("%s - moved by %s meters", axis.name(), distance)); break;
            case Z: gyroscopeZTextView.setText(String.format("%s - moved by %s meters", axis.name(), distance)); break;
        }
    }

    /*private void unregister() {
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }

    private void initViewAndRegister() {
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        Sensor linearAccelerometer = sensorManager.getDefaultSensor(sensorType);
        sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    record = true;
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    recordX = 0;
                    recordY = 0;
                    recordZ = 0;
                    record = false;
                }
                return false;
            }
        });
    }
    */

    public void onSensorChanged(SensorEvent event) {

        if (sensorType != Sensor.TYPE_LINEAR_ACCELERATION) {
            drawGraphs(event);
        } else {

            if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                holder.addAcceleration(event.values);

            } else if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
                float[] realWorldMove = holder.getRealWorldMove(event.values);
                for (int i = 0; i < realWorldMove.length; i++) {
                    pos[i] += realWorldMove[i];
                }
                gyroscopeXTextView.setText(String.format(FORMAT, pos[0]));
                gyroscopeYTextView.setText(String.format(FORMAT, pos[1]));
                gyroscopeZTextView.setText(String.format(FORMAT, pos[2]));


                //no.. tak todo - páč tohle je zlý :/ .. ale nějakým způsobem to dokáže zhruba určit pohyb..
                //bude hoodně potřeba ořezávat hodnoty dle gyra, páč to jsou randálky jak zmrd.. a to jenom na delay normal
                //a bude to chtít detekovat velké změny a pokud nějaké nastanou, tak začne věřit jednomu směru dokud nepřijde reakce
                //pomyslně rozkouskovat na etapy

            } else if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                //holder.computeMovement();
                Log.d("POS(" + System.currentTimeMillis() + ")", String.format("Moved by: %s north, %s east, %s up the sky", pos[0], pos[1], pos[2]));
            }
        }

    }

    private void drawGraphs(SensorEvent event) {
        float[] vec = event.values;
        if (vec[0] > 0) {
            float x = maxVal - Math.min(vec[0], maxVal);
            xPlus.setPadding(0, (int) (x * xPlus.getHeight()), 0, 0);
            xMinus.setPadding(0, 0, 0, xMinus.getHeight());
        } else {
            float x = -maxVal - Math.max(vec[0], -maxVal);
            xMinus.setPadding(0, 0, 0, (int) ((-x) * xMinus.getHeight()));
            xPlus.setPadding(0, 0, 0, xPlus.getHeight());
        }
        accelerometerXTextView.setText(String.format(FORMAT, vec[0]));
        if (record) {
            recordX += vec[0];
            gyroscopeXTextView.setText(String.format(FORMAT, recordX));
        }
        if (event.values.length < 2) return;
        if (vec[1] > 0) {
            float y = maxVal - Math.min(vec[1], maxVal);
            yPlus.setPadding(0, (int) (y * yPlus.getHeight()), 0, 0);
            yMinus.setPadding(0, 0, 0, yMinus.getHeight());
        } else {
            float y = -maxVal - Math.max(vec[1], -maxVal);
            yMinus.setPadding(0, 0, 0, (int) ((-y) * yMinus.getHeight()));
            yPlus.setPadding(0, 0, 0, yPlus.getHeight());
        }
        accelerometerYTextView.setText(String.format(FORMAT, vec[1]));
        if (record) {
            recordY += vec[0];
            gyroscopeYTextView.setText(String.format(FORMAT, recordY));
        }
        if (event.values.length < 3) return;
        if (vec[2] > 0) {
            float z = maxVal - Math.min(vec[2], maxVal);
            zPlus.setPadding(0, (int) (z * zPlus.getHeight()), 0, 0);
            zMinus.setPadding(0, 0, 0, zMinus.getHeight());
        } else {
            float z = -maxVal - Math.max(vec[2], -maxVal);
            zMinus.setPadding(0, 0, 0, (int) ((-z) * zMinus.getHeight()));
            zPlus.setPadding(0, 0, 0, zPlus.getHeight());
        }
        accelerometerZTextView.setText(String.format(FORMAT, vec[2]));
        if (record) {
            recordZ += vec[2];
            gyroscopeZTextView.setText(String.format(FORMAT, recordZ));
        }
        if (event.values.length < 4) return;
        if (vec[3] > 0) {
            float w = maxVal - Math.min(vec[3], maxVal);
            wPlus.setPadding(0, (int) (w * wPlus.getHeight()), 0, 0);
            wMinus.setPadding(0, 0, 0, wMinus.getHeight());
        } else {
            float w = -maxVal - Math.max(vec[3], -maxVal);
            wMinus.setPadding(0, 0, 0, (int) ((-w) * wMinus.getHeight()));
            wPlus.setPadding(0, 0, 0, wPlus.getHeight());
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

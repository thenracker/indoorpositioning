package cz.weissar.indoorpositioning.fragments;

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

import java.util.ArrayList;
import java.util.List;

import cz.weissar.indoorpositioning.R;
import cz.weissar.indoorpositioning.listeners.OnSensorMeasurement;
import cz.weissar.indoorpositioning.utils.sensor.SensorHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements OnSensorMeasurement {

    private TextView accelerometerXTextView, accelerometerYTextView, accelerometerZTextView;
    private TextView gyroscopeXTextView, gyroscopeYTextView, gyroscopeZTextView;
    private TextView rotationXTextView, rotationYTextView, rotationZTextView;

    private FrameLayout xPlus, xMinus, yPlus, yMinus, zPlus, zMinus;

    private boolean record = false;
    private List<Float> floats;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        accelerometerXTextView = ((TextView) view.findViewById(R.id.accelerometerXTextView));
        accelerometerYTextView = ((TextView) view.findViewById(R.id.accelerometerYTextView));
        accelerometerZTextView = ((TextView) view.findViewById(R.id.accelerometerZTextView));
        gyroscopeXTextView = ((TextView) view.findViewById(R.id.gyroscopeXTextView));
        gyroscopeYTextView = ((TextView) view.findViewById(R.id.gyroscopeYTextView));
        gyroscopeZTextView = ((TextView) view.findViewById(R.id.gyroscopeZTextView));
        rotationXTextView = ((TextView) view.findViewById(R.id.rotationXTextView));
        rotationYTextView = ((TextView) view.findViewById(R.id.rotationYTextView));
        rotationZTextView = ((TextView) view.findViewById(R.id.rotationZTextView));
        xPlus = ((FrameLayout) view.findViewById(R.id.xPlus));
        yPlus = ((FrameLayout) view.findViewById(R.id.yPlus));
        zPlus = ((FrameLayout) view.findViewById(R.id.zPlus));
        xMinus = ((FrameLayout) view.findViewById(R.id.xMinus));
        yMinus = ((FrameLayout) view.findViewById(R.id.yMinus));
        zMinus = ((FrameLayout) view.findViewById(R.id.zMinus));
        floats = new ArrayList<>();

        ((FloatingActionButton) view.findViewById(R.id.fabb)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    record = true;
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    record = false;
                    if(!floats.isEmpty()){
                        float total = 0.0f;
                        for (Float a : floats) {
                            total += a;
                        }
                        // cca * 5 do centimetrů dle posledních měření
                        rotationXTextView.setText("Uraženo " + total*5 + " cm");
                        floats.clear();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SensorHelper.getInstance().setListener(this);
        SensorHelper.getInstance().registerListeners();
    }

    @Override
    public void onPause() {
        SensorHelper.getInstance().unregisterListeners(); //prozatím máme jednoho, můžeme si dovolit
        super.onPause();
    }

    @Override
    public void updateEsteemedVector(float[] vec) {
        if (vec == null) return;

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

        gyroscopeXTextView.setText(format(vec[0]));
        gyroscopeYTextView.setText(format(vec[1]));
        gyroscopeZTextView.setText(format(vec[2]));

        if (record) {
            floats.add(vec[2]);
            //Log.d("GYRO", String.format("%.5f, %.5f, %.5f, %s ms", vec[0], vec[1], vec[2], System.currentTimeMillis()));
        }
    }

    private String format(double x) {
        return String.format("%.2f", x);
    }
}

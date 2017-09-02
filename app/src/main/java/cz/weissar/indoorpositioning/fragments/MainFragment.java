package cz.weissar.indoorpositioning.fragments;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.weissar.indoorpositioning.R;
import cz.weissar.indoorpositioning.listeners.OnSensorMeasurement;
import cz.weissar.indoorpositioning.utils.sensor.SensorHelper;
import cz.weissar.indoorpositioning.utils.sensor.Vector3D;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements OnSensorMeasurement {

    private TextView accelerometerXTextView, accelerometerYTextView, accelerometerZTextView;
    private TextView gyroscopeXTextView, gyroscopeYTextView, gyroscopeZTextView;
    private TextView magnetometerXTextView, magnetometerYTextView, magnetometerZTextView;
    private TextView rotationXTextView, rotationYTextView, rotationZTextView;

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
        magnetometerXTextView = ((TextView) view.findViewById(R.id.magnetometerXTextView));
        magnetometerYTextView = ((TextView) view.findViewById(R.id.magnetometerYTextView));
        magnetometerZTextView = ((TextView) view.findViewById(R.id.magnetometerZTextView));
        rotationXTextView = ((TextView) view.findViewById(R.id.rotationXTextView));
        rotationYTextView = ((TextView) view.findViewById(R.id.rotationYTextView));
        rotationZTextView = ((TextView) view.findViewById(R.id.rotationZTextView));
        
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
    public void onNewMeasure(float azimut, float pitch, float roll) {
        //magnetometerXTextView.setText(format(Math.toDegrees((double) azimut)));
        //magnetometerYTextView.setText(format(Math.toDegrees((double) pitch)));
        //magnetometerZTextView.setText(format(Math.toDegrees((double) roll)));
    }

    @Override
    public void onVectorChanged(Vector3D vec) {
        switch (vec.getType()) {
            case TYPE_ACCELEROMETER:
                accelerometerXTextView.setText(format(vec.getX()));
                accelerometerYTextView.setText(format(vec.getY()));
                accelerometerZTextView.setText(format(vec.getZ()));
                break;
            case TYPE_GYROSCOPE:
                gyroscopeXTextView.setText(format(vec.getX()));
                gyroscopeYTextView.setText(format(vec.getY()));
                gyroscopeZTextView.setText(format(vec.getZ()));
                break;
            case TYPE_MAGNETIC_FIELD:
                magnetometerXTextView.setText(format(vec.getX()));
                magnetometerYTextView.setText(format(vec.getY()));
                magnetometerZTextView.setText(format(vec.getZ()));
                break;
            case TYPE_ROTATION_VECTOR:
                rotationXTextView.setText(format(vec.getX()));
                rotationYTextView.setText(format(vec.getY()));
                rotationZTextView.setText(format(vec.getZ()));
                break;
        }
    }

    private String format(double x) {
        return String.format("%.1f", x);
    }
}

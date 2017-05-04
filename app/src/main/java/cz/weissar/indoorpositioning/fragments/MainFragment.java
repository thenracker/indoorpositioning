package cz.weissar.indoorpositioning.fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.weissar.indoorpositioning.R;
import cz.weissar.indoorpositioning.listeners.OnSensorMeasurement;
import cz.weissar.indoorpositioning.utils.sensor.SensorHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements OnSensorMeasurement {

    private TextView helloTextView;
    private TextView helloTextView2;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //SensorHelper.getInstance().setListener(this);
        helloTextView = (TextView) view.findViewById(R.id.helloTextView);
        helloTextView2 = (TextView) view.findViewById(R.id.helloTextView2);
    }

    @Override
    public void onResume() {
        super.onResume();
        SensorHelper.getInstance().setListener(this);
    }

    @Override
    public void onStop() {
        SensorHelper.getInstance().unsetListener();
        super.onStop();
    }

    @Override
    public void onNewMeasure(SensorEvent event) {
        String sensorName = event.sensor.getName();
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            helloTextView.setText(sensorName + ":\nX: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");
        }else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            helloTextView2.setText(sensorName + ":\nX: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");
        }
    }
}

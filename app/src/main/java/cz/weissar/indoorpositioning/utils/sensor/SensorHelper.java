package cz.weissar.indoorpositioning.utils.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import cz.weissar.indoorpositioning.IndoorPositioningApp;
import cz.weissar.indoorpositioning.listeners.OnSensorMeasurement;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

/**
 * Created by petrw on 04.05.2017.
 */

public class SensorHelper implements SensorEventListener {

    private static final String TAG = "SENSORS";
    private static final SensorHelper ourInstance = new SensorHelper();

    public static SensorHelper getInstance() {
        return ourInstance;
    }

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Sensor gyroscope;

    private OnSensorMeasurement onSensorMeasurementListener;

    private Vector3D vecAccelerometer;
    private Vector3D vecGyroscope;

    private SensorHelper() {
        sensorManager = (SensorManager) IndoorPositioningApp.getContext().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);
    }

    public void setListener(OnSensorMeasurement listener) {
        this.onSensorMeasurementListener = listener;
    }

    public void unsetListener() {
        this.onSensorMeasurementListener = null;
    }

    public void registerListeners() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (onSensorMeasurementListener != null) {
            //onSensorMeasurementListener.onNewMeasure(event);
        }

        if (event.sensor.getType() == TYPE_ACCELEROMETER) {
            handleSensorChanged(vecAccelerometer, event);
        }
        if (event.sensor.getType() == TYPE_GYROSCOPE) {
            handleSensorChanged(vecGyroscope, event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void handleSensorChanged(Vector3D vec, SensorEvent event){
        if (vec == null) {
            if(event.sensor.getType() == TYPE_ACCELEROMETER){
                vecAccelerometer = Vector3D.newInstance(event.values[0], event.values[1], event.values[2], event.sensor.getType());
            }
            if(event.sensor.getType() == TYPE_GYROSCOPE){
                vecGyroscope = Vector3D.newInstance(event.values[0], event.values[1], event.values[2], event.sensor.getType());
            }
        } else {
            vec.newValues(event.values[0], event.values[1], event.values[2]);
            onSensorMeasurementListener.onVectorChanged(vec);
        }
    }
}

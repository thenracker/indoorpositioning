package cz.weissar.indoorpositioning.utils.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import cz.weissar.indoorpositioning.IndoorPositioningApp;
import cz.weissar.indoorpositioning.listeners.OnSensorMeasurement;

import static android.content.Context.SENSOR_SERVICE;
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

    private SensorHelper() {
        sensorManager = (SensorManager) IndoorPositioningApp.getContext().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);
    }

    public void setListener(OnSensorMeasurement listener){
        this.onSensorMeasurementListener = listener;
    }
    public void unsetListener(){
        this.onSensorMeasurementListener = null;
    }

    public void registerListeners(){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregisterListeners(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(onSensorMeasurementListener != null){
            onSensorMeasurementListener.onNewMeasure(event);
        }else{
            String sensorName = event.sensor.getName();
            Log.d(TAG, sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

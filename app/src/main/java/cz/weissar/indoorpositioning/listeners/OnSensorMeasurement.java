package cz.weissar.indoorpositioning.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import cz.weissar.indoorpositioning.utils.sensor.Vector3D;

/**
 * Created by petrw on 04.05.2017.
 */

public interface OnSensorMeasurement {

    public static final int TYPE_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    public static final int TYPE_GYROSCOPE = Sensor.TYPE_GYROSCOPE;

    void onNewMeasure(SensorEvent event);

    void onVectorChanged(Vector3D vec);

}

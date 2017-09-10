package cz.weissar.indoorpositioning.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import cz.weissar.indoorpositioning.utils.sensor.Vector3D;

/**
 * Created by petrw on 04.05.2017.
 */

public interface OnSensorMeasurement {

    void updateEsteemedVector(float[] vec);

    //void updateEsteemedVector(float x, float y, float z);

    //void onNewMeasure(float azimut, float pitch, float roll);

    //void onVectorChanged(Vector3D vec);

}

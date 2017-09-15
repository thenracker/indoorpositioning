package cz.weissar.indoorpositioning.old.listeners;

import android.hardware.SensorEvent;

/**
 * Created by petrw on 04.05.2017.
 */

public interface OnSensorMeasurement {

    void updateEsteemedVector(float[] vec);

    //void updateEsteemedVector(float x, float y, float z);

    void onNewMeasure(SensorEvent event);

    //void onVectorChanged(Vector3D vec);

}

package cz.weissar.indoorpositioning.listeners;

import android.hardware.SensorEvent;

/**
 * Created by petrw on 04.05.2017.
 */

public interface OnSensorMeasurement {

    void onNewMeasure(SensorEvent event);

}

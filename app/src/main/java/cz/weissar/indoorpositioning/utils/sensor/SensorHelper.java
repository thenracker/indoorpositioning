package cz.weissar.indoorpositioning.utils.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import cz.weissar.indoorpositioning.IndoorPositioningApp;
import cz.weissar.indoorpositioning.listeners.OnSensorMeasurement;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

/**
 * Created by petrw on 04.05.2017.
 */

public class SensorHelper implements SensorEventListener {

    private static final String TAG = SensorHelper.class.getSimpleName();
    private static final SensorHelper instance = new SensorHelper();

    public static SensorHelper getInstance() {
        return instance;
    }

    private SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Sensor gyroscope;
    private final Sensor magnetometer;

    private OnSensorMeasurement onSensorMeasurementListener;

    private Vector3D vecAccelerometer;
    private Vector3D vecGyroscope;
    private Vector3D vecMagnetometer;

    private SensorHelper() {
        sensorManager = (SensorManager) IndoorPositioningApp.getContext().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD);
    }

    public void setListener(OnSensorMeasurement listener) {
        this.onSensorMeasurementListener = listener;
    }

    //TAK JO .. akcelerometr

    /**
     * AKCELEROMETR
     * x - levý bok = 10, pravý bok = -10 (jinak se blíží nule)
     * y - hlava nahoru 10, hlava dolů = -10 (jinak k nule)
     * z - telefon leží na zádech = 10, na břiše = -10 (v ostatních pozicích se blíží k nule)
     *
     *
     * GYROSKOP (pokud je hlava nahoře - jakobych fotil na výšku)
     * x - pohyb nahoru plusové hodnoty, dolů mínusové hodnoty
     * y - doleva jdeme k plusovým hodnotám (skoro až k jedné), doprava jdeme do mínusu
     * z - ode mě plus, ke mně mínus
     *
     * GYRO (když telefon leží na stole)
     * x - nahoru ke mně plusové hodnoty, dolů mínusové
     * y - :( nic
     * z - pohyb doleva na stole plusové hodnoty, doprava mínusové
     * z - pohyb dopředu plusové hodnoty, pohyb dozadu mínusové
     *
     */

    public void registerListeners() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == TYPE_ACCELEROMETER) {
            handleSensorChanged(vecAccelerometer, event);
        }
        if (event.sensor.getType() == TYPE_GYROSCOPE) {
            handleSensorChanged(vecGyroscope, event);
        }
        if (event.sensor.getType() == TYPE_MAGNETIC_FIELD) {
            handleSensorChanged(vecMagnetometer, event);
        }

        /*if (vecAccelerometer != null && vecGyroscope != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            if (SensorManager.getRotationMatrix(R, I, vecAccelerometer.getValues(), vecGyroscope.getValues())) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                onSensorMeasurementListener.onNewMeasure(orientation[0], orientation[1], orientation[2]);
            }
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void handleSensorChanged(Vector3D vec, SensorEvent event) {
        if (vec == null) {
            if (event.sensor.getType() == TYPE_ACCELEROMETER) {
                vecAccelerometer = Vector3D.newInstance(event.values[0], event.values[1], event.values[2], event.sensor.getType());
            }
            if (event.sensor.getType() == TYPE_GYROSCOPE) {
                vecGyroscope = Vector3D.newInstance(event.values[0], event.values[1], event.values[2], event.sensor.getType());
            }
            if (event.sensor.getType() == TYPE_MAGNETIC_FIELD) {
                vecMagnetometer = Vector3D.newInstance(event.values[0], event.values[1], event.values[2], event.sensor.getType());
            }
        } else {
            vec.newValues(event.values[0], event.values[1], event.values[2]);
            onSensorMeasurementListener.onVectorChanged(vec);
        }
    }
}

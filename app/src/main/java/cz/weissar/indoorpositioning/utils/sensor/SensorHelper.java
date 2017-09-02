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
import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;

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
    private final Sensor rotation;

    private OnSensorMeasurement onSensorMeasurementListener;

    private Vector3D vecAccelerometer;
    private Vector3D vecGyroscope;
    private Vector3D vecMagnetometer;
    private Vector3D vecRotation;

    private SensorHelper() {
        sensorManager = (SensorManager) IndoorPositioningApp.getContext().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD);
        rotation = sensorManager.getDefaultSensor(TYPE_ROTATION_VECTOR);
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
     * <p>
     * <p>
     * GYROSKOP (pokud je hlava nahoře - jakobych fotil na výšku)
     * x - pohyb nahoru plusové hodnoty, dolů mínusové hodnoty
     * y - doleva jdeme k plusovým hodnotám (skoro až k jedné), doprava jdeme do mínusu
     * z - ode mě plus, ke mně mínus
     * <p>
     * GYRO (když telefon leží na stole)
     * x - nahoru ke mně plusové hodnoty, dolů mínusové
     * y - :( nic
     * z - pohyb doleva na stole plusové hodnoty, doprava mínusové
     * z - pohyb dopředu plusové hodnoty, pohyb dozadu mínusové
     */

    public void registerListeners() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        /*if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            float[] rotationMatrixRemapped = new float[16];
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrixRemapped);

            float[] orientationVals = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientationVals);
        }*/

        showOnScreen(event);

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

    private void showOnScreen(SensorEvent event) {
        if (event.sensor.getType() == TYPE_ACCELEROMETER) {
            handleSensorChanged(vecAccelerometer, event);
        }
        if (event.sensor.getType() == TYPE_GYROSCOPE) {
            handleSensorChanged(vecGyroscope, event);
        }
        if (event.sensor.getType() == TYPE_MAGNETIC_FIELD) {
            handleSensorChanged(vecMagnetometer, event);
        }
        if (event.sensor.getType() == TYPE_ROTATION_VECTOR) {
            handleSensorChanged(vecRotation, event);
        }
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
            if (event.sensor.getType() == TYPE_ROTATION_VECTOR) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

                float[] rotationMatrixRemapped = new float[16];

                /*
                Using the camera (Y axis along the camera's axis) for an augmented reality application where the rotation angles are needed:
                    remapCoordinateSystem(inR, AXIS_X, AXIS_Z, outR);
                Using the device as a mechanical compass when rotation is Surface.ROTATION_90:
                    remapCoordinateSystem(inR, AXIS_Y, AXIS_MINUS_X, outR);
                 */
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrixRemapped);

                float[] orientationVals = new float[3];
                SensorManager.getOrientation(rotationMatrixRemapped, orientationVals);

                // Optionally convert the result from radians to degrees
                /*orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
                orientationVals[1] = (float) Math.toDegrees(orientationVals[1]);
                orientationVals[2] = (float) Math.toDegrees(orientationVals[2]);*/

                vecRotation = Vector3D.newInstance(orientationVals[0], orientationVals[1], orientationVals[2], event.sensor.getType());
            }
        } else {
            vec.newValues(event.values[0], event.values[1], event.values[2]);
            onSensorMeasurementListener.onVectorChanged(vec);
        }
    }

    private double max = Math.PI / 2 - 0.01;
    private double min = -max;

    private float[] rotationVectorAction(float[] values) {
        float[] result = new float[3];
        float vec[] = values;
        float quat[] = new float[4];
        float[] orientation = new float[3];
        SensorManager.getQuaternionFromVector(quat, vec);
        float[] rotMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rotMat, quat);
        SensorManager.getOrientation(rotMat, orientation);
        result[0] = (float) orientation[0];
        result[1] = (float) orientation[1];
        result[2] = (float) orientation[2];
        return result;
    }

    private void main(float[] sensorInput) { //pohledový vektor dle natočení telefonu (pro OpenGL ES)
        float yaw, pitch;
        float[] result = rotationVectorAction(sensorInput);
        yaw = result[0];
        pitch = result[1];
        pitch = (float) Math.max(min, pitch);
        pitch = (float) Math.min(max, pitch);
        float dx = (float) (Math.sin(yaw) * (-Math.cos(pitch)));
        float dy = (float) Math.sin(pitch);
        float dz = (float) (Math.cos(yaw) * Math.cos(pitch));
    }
}

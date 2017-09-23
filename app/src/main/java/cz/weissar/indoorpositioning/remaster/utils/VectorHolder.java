package cz.weissar.indoorpositioning.remaster.utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import cz.weissar.indoorpositioning.remaster.scene.interfaces.MovementChange;

/**
 * Created by petrw on 17.09.2017.
 */

public class VectorHolder implements SensorEventListener {

    float NOISE = 0.6f;
    int SIZE = 8192;

    public enum Axis {X, Y, Z}

    float[] ax, ay, az; //pole hodnot
    long[] lx, ly, lz;
    int ix, iy, iz; //ukazatele do polí

    Boolean gx1, gy1, gz1, gx2, gy2, gz2;
    boolean cx, cy, cz = false;
    SensorManager manager;
    MovementChange listener;

    /**
     * Vytvořit holdera až při prvním měření hodnot
     */
    public VectorHolder(MovementChange listener) {
        ax = new float[SIZE];
        ay = new float[SIZE];
        az = new float[SIZE];
        lx = new long[SIZE];
        ly = new long[SIZE];
        lz = new long[SIZE];
        this.listener = listener;
    }

    public void register(SensorManager sensorManager) {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_FASTEST);

        this.manager = sensorManager;
    }

    public void unregister() {
        manager.unregisterListener(this);
    }

    //když detect movement najde akci a reakci, spočítáme nad celým souborem pohyb
    public void computeMovement(Axis x) {

        float[] a;
        long[] l;
        int p;
        switch (x){
            default:
            case X: a = ax; l = lx; p = ix; break;
            case Y: a = ay; l = ly; p = iy; break;
            case Z: a = az; l = lz; p = iz; break;
        }
        double speed = 0f; // m/s
        double distance = 0f; // meters
        double time = 0;
        for (int i = 1; i < p; i++) {
            time = ((double)l[i] - (double)l[i-1]) / 1000; //transfer to seconds from ms
            //speed += (ax[i] * (time * time) / 2);
            speed += time * a[i];
            distance += time * speed;
        }
        //data od nuly do ia - dohledáme timestamps a rychlost vzrůstu atd..
        //a to bude ono <3
        newEpisode(x);

        listener.onMovement(x, distance);
    }

    private void newEpisode(Axis x) {
        switch (x) {
            case X:
                ix = 0;
                gx1 = gx2 = null;
                cx = false;
                break;
            case Y:
                iy = 0;
                gy1 = gy2 = null;
                cy = false;
                break;
            case Z:
                iz = 0;
                gz1 = gz2 = null;
                cz = false;
                break;
        }
    }

    /**
     * @param values Values from TYPE_ROTATION_VECTOR ONLY!!!
     * @return returns float[3] of XYZ world coordinations (+ay to North, +ax to East, -Z to middle Earth (gravity pole))
     */
    public float[] getRealWorldMove(float[] values) { //float[]{x, y, z, θ]
        float[] rotMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rotMat, values);

        float[] realPos = new float[3]; // = mat * vec;

        //realPos[0] = rotMat[0] * posX + rotMat[1] * posY + rotMat[2] * posZ;
        //realPos[1] = rotMat[3] * posX + rotMat[4] * posY + rotMat[5] * posZ;
        //realPos[2] = rotMat[6] * posX + rotMat[7] * posY + rotMat[8] * posZ;

        return realPos;
    }

    /**
     * ACCELERATION a = deltaPosition / deltaTime
     */

    /**
     * Reads 3 dimensions and crops noise
     *
     * @param data float[3]
     */
    public void addAcceleration(float... data) {
        cropNoise(data);
        ax[ix] = data[0];
        ay[iy] = data[1];
        az[iz] = data[2];
        lx[ix] = ly[iy] = lz[iz] = System.currentTimeMillis();

        ix = (ix + 1) % SIZE;
        iy = (iy + 1) % SIZE;
        iz = (iz + 1) % SIZE;

        detectMovement(data);
    }

    private void detectMovement(float... data) {
        detectMovement(data[0], Axis.X);
        detectMovement(data[1], Axis.Y);
        detectMovement(data[2], Axis.Z);
    }

    private void detectMovement(float a, Axis is) {
        if (is == Axis.X){
            if (a == 0){
                if (gx1 != null && gx2 == null){
                    cx = true;
                } else if (gx1 != null && gx2 != null){
                    if (gx1.equals(gx2)) {
                        //todo co s daty - teoreticky mohou gxýt užitečná
                        //co když někdo zrychlil telefon a těsně před reakcí zase zrychlil? -_- fuck
                        newEpisode(is);
                    } else {
                        //akce a reakce <3 - spočítejme pohyg
                        computeMovement(is);
                    }
                }
            }
            if (gx1 == null) {
                if (a > NOISE) {
                    gx1 = true;
                } else if (a < -NOISE) {
                    gx1 = false;
                }
            }
            if (cx && gx2 == null) {
                if (a > NOISE) {
                    gx2 = true;
                } else if (a < -NOISE) {
                    gx2 = false;
                }
            }
        }
        if (is == Axis.Y){
            if (a == 0){
                if (gy1 != null && gy2 == null){
                    cy = true;
                } else if (gy1 != null && gy2 != null){
                    if (gy1.equals(gy2)) {
                        //todo co s daty - teoreticky mohou gyýt užitečná
                        //co když někdo zrychlil telefon a těsně před reakcí zase zrychlil? -_- fuck
                        newEpisode(is);
                    } else {
                        //akce a reakce <3 - spočítejme pohyg
                        computeMovement(is);
                    }
                }
            }
            if (gy1 == null) {
                if (a > NOISE) {
                    gy1 = true;
                } else if (a < -NOISE) {
                    gy1 = false;
                }
            }
            if (cy && gy2 == null) {
                if (a > NOISE) {
                    gy2 = true;
                } else if (a < -NOISE) {
                    gy2 = false;
                }
            }
        }

        if (is == Axis.Z){
            if (a == 0){
                if (gz1 != null && gz2 == null){
                    cz = true;
                } else if (gz1 != null && gz2 != null){
                    if (gz1.equals(gz2)) {
                        //todo co s datz - teoretickz mohou gzýt užitečná
                        //co kdzž někdo zrzchlil telefon a těsně před reakcí zase zrzchlil? -_- fuck
                        newEpisode(is);
                    } else {
                        //akce a reakce <3 - spočítejme pohzg
                        computeMovement(is);
                    }
                }
            }
            if (gz1 == null) {
                if (a > NOISE) {
                    gz1 = true;
                } else if (a < -NOISE) {
                    gz1 = false;
                }
            }
            if (cz && gz2 == null) {
                if (a > NOISE) {
                    gz2 = true;
                } else if (a < -NOISE) {
                    gz2 = false;
                }
            }
        }

    }

    public void addRotationVector(float... data) { //float[]{x, y, z, θ]

    }

    /**
     * Crops sensor noise
     *
     * @param data float[n]
     */
    private void cropNoise(float... data) {
        for (int i = 0; i < data.length; i++) {
            if (Math.abs(data[i]) < NOISE) {
                data[i] = 0;
            } else {
                data[i] = Math.round(data[i] * 1000f) / 1000f;
            }
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            addAcceleration(event.values);

        } else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

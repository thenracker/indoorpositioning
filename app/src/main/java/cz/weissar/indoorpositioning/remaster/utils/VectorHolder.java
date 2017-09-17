package cz.weissar.indoorpositioning.remaster.utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by petrw on 17.09.2017.
 */

public class VectorHolder implements SensorEventListener {

    float posX, posY, posZ;

    long lastTimestamp;
    float x, y, z;

    float speedX, speedY, speedZ;
    final static float DAMPING = 0.001f; //tlumení za 1ms
    final static float SPEED_GROW = 1.000f; //síla růstu rychlosti za 1 jednotku přetížení (zatim necháme 1)

    /**
     * Could be used when a step is detected - the speed should stay the same
     */
    public void startNewEpisode() {
        posX = posY = posZ = 0f;
    }

    /**
     * @param values Values from TYPE_ROTATION_VECTOR ONLY!!!
     * @return returns float[3] of XYZ world coordinations (+Y to North, +X to East, -Z to middle Earth (gravity pole))
     */
    public float[] getRealWorldMove(float[] values) { //float[]{x, y, z, θ]
        float[] rotMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rotMat, values);

        float[] realPos = new float[3]; // = mat * vec;

        realPos[0] = rotMat[0] * posX + rotMat[1] * posY + rotMat[2] * posZ;
        realPos[1] = rotMat[3] * posX + rotMat[4] * posY + rotMat[5] * posZ;
        realPos[2] = rotMat[6] * posX + rotMat[7] * posY + rotMat[8] * posZ;

        return realPos;
    }

    /**
     * ACCELERATION a = deltaPosition / deltaTime
     */

    /**
     * Reads 3 dimensions and crops noise
     *
     * @param timeStamp
     * @param data      float[3]
     */
    public void addAcceleration(long timeStamp, float... data) {
        cropNoise(data);
        //cropByGyro??
        computeSpeed(timeStamp, data[0], data[1], data[2]);
        computePosition();
        damp();
    }

    //teoreticky addGyro? Na eliminaci vzniku akcelerace rotací???

    private void computeSpeed(long timeStamp, float x, float y, float z) {

        //float m = (float)(timeStamp - lastTimestamp);
        speedX = x;
        speedY = y;
        speedZ = z;
        //speedX += x * m * SPEED_GROW;
        //speedY += y * m * SPEED_GROW;
        //speedZ += z * m * SPEED_GROW;

        this.lastTimestamp = timeStamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void computePosition() {
        //teoreticky by měla mít pozice nějakou prodlevu - takhle kopíruje speedXYZ - uvidíme časem
        posX += speedX;
        posY += speedY;
        posZ += speedZ;
    }

    /**
     * Crops sensor noise
     *
     * @param data float[n]
     */
    private void cropNoise(float... data) {
        for (int i = 0; i < data.length; i++) {
            if (Math.abs(data[i]) < 0.5f) {
                data[i] = 0;
            } else {
                data[i] = Math.round(data[i] * 100f) / 100f;
            }
        }
    }

    /**
     * Simulates damp of speed
     */
    private void damp() {
        speedX = speedX == 0? 0 : speedX > 0 ? speedX - DAMPING : speedX + DAMPING;
        speedY = speedY == 0? 0 : speedY > 0 ? speedY - DAMPING : speedY + DAMPING;
        speedZ = speedZ == 0? 0 : speedZ > 0 ? speedZ - DAMPING : speedZ + DAMPING;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

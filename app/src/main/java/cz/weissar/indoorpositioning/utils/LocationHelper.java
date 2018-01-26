package cz.weissar.indoorpositioning.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petrw on 11.11.2017.
 */

public class LocationHelper implements SensorEventListener {

    private static final float STEP_GRAVITY_DRIFT = 0.8f;

    private static final float STEP_LENGTH = 68.25f;
    private static final float NON_GRAVITY_MOVEMENT_FLOW = 5f;
    private static final float GYRO_SIG = 0.9f;
    private static final long MIN_STEP_TIME_LENGTH = 500;
    private static final float SIG_WALK = 1.7f;
    private static float NOISE = 0.25f;
    private long lastStepTimestamp = 0;

    private static int SIZE = 8192; //uvidíme co si můžeme dovolit s RAMkami
    private static int SIZE_PRESSURE = 150;

    private int lastFloor = -1;

    private float[] valuesA, valuesG, valuesGr, valuesR, valuesP;

    private long[] timeA, timeG, timeGr, timeR;
    private int pG, pGr, pA, pR, pP;

    private float outsidePressure, temperature;

    private static int[] sensors = new int[]{
            Sensor.TYPE_LINEAR_ACCELERATION, Sensor.TYPE_GRAVITY, Sensor.TYPE_ROTATION_VECTOR,
            Sensor.TYPE_GYROSCOPE, Sensor.TYPE_PRESSURE,
            /*Sensor.TYPE_STEP_DETECTOR,
            Sensor.TYPE_ACCELEROMETER, */
            Sensor.TYPE_LIGHT};

    private static int[] fewSensors = new int[]{Sensor.TYPE_PRESSURE};

    private static List<LocationListener> listeners;
    private static final LocationHelper instance = new LocationHelper();
    private int state = 0;

    private Location location;
    private Building building;

    private List<MapUtils.LatLng> possibleSpots;
    private boolean precise;
    MapUtils.LatLng nowPos;

    public static LocationHelper get() {
        return instance;
    }

    private LocationHelper() {
        listeners = new ArrayList<>();
        valuesA = new float[SIZE * 3];
        valuesG = new float[SIZE * 3];
        valuesGr = new float[SIZE * 3];
        valuesR = new float[SIZE * 4];
        valuesP = new float[SIZE_PRESSURE];
        timeA = new long[SIZE];
        timeG = new long[SIZE];
        timeGr = new long[SIZE];
        timeR = new long[SIZE];
        pA = 0;
        pG = 0;
        pGr = 0;
        pR = 0;
        pP = 0;
        outsidePressure = 0;
        temperature = 0;
    }

    public void updatePressure() throws Exception {
        URL pressureUrl = new URL("http://www.in-pocasi.cz/meteostanice/stanice.php?stanice=hradec");

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(pressureUrl.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        PressureGetterHelper.process(builder.toString(), new PressureGetterHelper.Callback() {
            @Override
            public void response(float pressureAtSeaLevel, float temperatureOut) {
                outsidePressure = pressureAtSeaLevel;
                temperature = temperatureOut;
            }
        });
    }

    public void shouldStartComputing(boolean shouldStart, Context context, boolean precise) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        int[] nowSensors = precise ? sensors : fewSensors;
        for (int sensor : nowSensors) {
            if (shouldStart) {
                //pokud neexistuje senzor, prostě se vrátí false a nebude se počítat
                sensorManager.registerListener(this,
                        sensorManager.getDefaultSensor(sensor),
                        SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                sensorManager.unregisterListener(this,
                        sensorManager.getDefaultSensor(sensor));
            }
        }

        this.precise = precise;
        this.nowPos = new MapUtils.LatLng(0, 0);

        if (precise) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //TODO připravit dialog
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LocationHelper.this.location = location;
                    double dist = Double.MAX_VALUE;
                    for (Building building : Building.values()) {
                        float nowDist = MapUtils.distanceInMeters(location, building.getLat(), building.getLng());
                        if (nowDist < dist) {
                            dist = nowDist;
                            LocationHelper.this.building = building;
                        }
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

        //update tlaku
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    updatePressure();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                handleLinearAccelerationMeasure(event.values, event.timestamp);
                break;
            case Sensor.TYPE_GRAVITY:
                handleGravityMeasure(event.values, event.timestamp);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                handleRotationVectorMeasure(event.values, event.timestamp);
                break;
            case Sensor.TYPE_GYROSCOPE:
                handleGyroscopeMeasure(event.values, event.timestamp);
                break;

            /*
            case Sensor.TYPE_ACCELEROMETER:
                //detectSteps(event.values, event.timestamp); //custom step detector
                break;

            case Sensor.TYPE_STEP_DETECTOR:
                handleMovementEpisode(true, event.timestamp);
                break;
            */

            case Sensor.TYPE_PRESSURE:
                handlePressureMeasure(event.values[0], event.timestamp);
                break;
            case Sensor.TYPE_LIGHT:
                //light = event.values[0];
                break;
        }
    }

    private float clearNoise(float val) {
        return val > NOISE ? val : (val < -NOISE ? val : 0f);
    }

    private void decreaseAccordingToCenteredForce() { //3 hodnoty

        int iGyro = 0;
        for (int i = 0; i < pA; i += 3) {

            //float avgGyroX = 0;
            float avgGyroY = 0;
            float avgGyroZ = 0;

            while (timeG[iGyro / 3] < timeA[i / 3] && iGyro < SIZE) {
                //avgGyroX += valuesG[iGyro];
                avgGyroY += valuesG[iGyro + 1];
                avgGyroZ += valuesG[iGyro + 2];

                iGyro += 3;
            }

            //avgGyroX /= (iGyro / 3);
            avgGyroY /= (iGyro / 3);
            avgGyroZ /= (iGyro / 3);

            float absZ = Math.abs(avgGyroZ);
            if (absZ > GYRO_SIG) {
                if (absZ > (2 * GYRO_SIG)) {
                    valuesA[i + 1] = 0;
                } else {
                    valuesA[i + 1] *= ((2 * GYRO_SIG) - absZ);
                }
            }

            float absY = Math.abs(avgGyroY);
            if (absY > GYRO_SIG) {
                if (absY > (2 * GYRO_SIG)) {
                    valuesA[i + 2] = 0;
                } else {
                    valuesA[i + 2] *= ((2 * GYRO_SIG) - absY);
                }
            }
        }

        /*//TODO předělat na void - budeme rovnou tady upravovat hodnoty akcelerometru ;)
        float mX = 0;
        float mY = 0;
        float mZ = 0;

        //TODO - takhle je to pro celý cyklus kroku.. úplně tim v podstatě killneme ten krok..
        //TODO - chtělo by to jen přičítat kolik z celkového času zde jsou signifikantní a o to to zmenšovat..
        //TODO .. budou hodnoty na Z třeba 0 0 2 5 3 0 0 .. takže bychom to měli násobit 4/7 se gyro nehlo takže to bylo spolehlivé
        // TODO - ale to je na prd celkem.. spíš by to chtělo podle timestampu rovnou tady upravit hodnoty akcelerometru
        //a přepsat je - zmenšit je, je-li třeba

        for (int i = 0; i < pG; i += 3) {

            //Math.abs
            float nX = valuesG[i] < 0 ? -valuesG[i] : valuesG[i];
            float nY = valuesG[i + 1] < 0 ? -valuesG[i + 1] : valuesG[i + 1];
            float nZ = valuesG[i + 2] < 0 ? -valuesG[i + 2] : valuesG[i + 2];

            //Hledáme nejvyšší hodnotu gyroskopu
            mX = nX > mX ? mX : nX;
            mY = nY > mY ? mY : nY;
            mZ = nZ > mZ ? mZ : nZ;
        }

        //je-li gyroskop hodnota významná, pak budeme osu akcelerometru vynechávat pro výpočet - viz níže
        //Takže TODO ne úplně na nulu, ale v poměru snížit viz nějako dle grafu
        float[] r = new float[]{1, 1, 1};
        if (mX > GYRO_SIG) {
            r[2] = 0;
        }

        *//*if (mY > GYRO_SIG){ //moc se nás netýká..?!}*//*

        if (mZ > GYRO_SIG) {
            r[0] = 0;
            r[1] = 0;
        }
        return r;*/
    }

    private void handleLinearAccelerationMeasure(float[] vals, long timestamp) {
        valuesA[pA] = clearNoise(vals[0]);
        valuesA[pA + 1] = clearNoise(vals[1]);
        valuesA[pA + 2] = clearNoise(vals[2]);
        timeA[pA / 3] = timestamp;
        pA = (pA + 3) % (valuesA.length);
        detectSteps(timestamp);
    }

    private void handleGravityMeasure(float[] vals, long timestamp) {
        valuesGr[pGr] = (vals[0]);
        valuesGr[pGr + 1] = (vals[1]);
        valuesGr[pGr + 2] = (vals[2]);
        timeGr[pGr / 3] = timestamp;
        pGr = (pGr + 3) % (valuesGr.length);
    }

    private void handleRotationVectorMeasure(float[] vals, long timestamp) {
        valuesR[pR] = vals[0];
        valuesR[pR + 1] = vals[1];
        valuesR[pR + 2] = vals[2];
        valuesR[pR + 3] = vals[3];
        timeR[pR / 4] = timestamp;
        pR = (pR + 4) % (valuesR.length);
    }


    private void handleGyroscopeMeasure(float[] vals, long timestamp) {
        valuesG[pG] = (vals[0]);
        valuesG[pG + 1] = (vals[1]);
        valuesG[pG + 2] = (vals[2]);
        timeG[pG / 3] = timestamp;
        pG = (pG + 3) % (valuesG.length);
    }

    private void handlePressureMeasure(float value, long timestamp) {
        if (outsidePressure != 0 || building != null) {

            if (pP == SIZE_PRESSURE) {
                float avgPressure = 0;
                float max = -Float.MAX_VALUE;
                float min = Float.MAX_VALUE;

                //průměrný tlak z hodnot
                for (float f : valuesP) {
                    avgPressure += f;
                    max = max < f ? f : max;
                    min = f < min ? f : min;
                }
                avgPressure /= SIZE_PRESSURE;

                //detekce přestřelů
                if (Math.abs(max - min) > 1.5f) {
                    //uživatel tlačil na displej či jinačí zvěrstva, která zmařila správné výpočty
                    //v takovou chvíli nechceme publitkovat výsledky !
                    pP = 0;
                    return;
                }

                float height = SensorManager.getAltitude(outsidePressure, avgPressure);
                float hypsoAlt = hypsometricAltitude(temperature, outsidePressure, avgPressure); //porovnávat todo
                //float floor = building.getFloorForAltitude(height, outsidePressure);

                /*if (floor != lastFloor) {
                    //jsme u schodiště - todo využít - jsme někde u schodů - takže pokud je GPS něco jako 0 0 tak se od toho odrazíme
                    //TODO building.getPossibleSestupy a do seznamu latLngs které budeme škrtat
                    possibleSpots = new ArrayList<>();
                    for (MapUtils.LatLng latLng : building.possibleStairs()){
                        possibleSpots.add(latLng);
                    }
                }

                lastFloor = (int) floor;*/

                //onFloorDetected(lastFloor);

                //LOG
                //builder = new StringBuilder("timestamp;avgPressure;outsidePressure;sensorManagerAltitude;hypsometricAltitude\n");
                if (!precise) {
                    for (LocationListener listener : listeners) {
                        listener.onValuesMeasured("" + timestamp, "" + avgPressure, "" + outsidePressure, "" + temperature, "" + height, "" + hypsoAlt);
                    }
                }


                pP = 0;

                System.out.println(String.format("Venkovní tlak: %s, Náš tlak: %s Vypočtená výška: %s",
                        outsidePressure, avgPressure, height));


                /*Toast.makeText(UhkHelperApp.getInstance().getApplicationContext(),
                        String.format("Venkovní tlak: %s, Náš tlak: %s Vypočtená výška: %s, HypsoVýška: %s",
                                outsidePressure, avgPressure, height, hypsoAlt), Toast.LENGTH_LONG).show();*/


            }

            valuesP[pP] = value;
            pP++;
        }
    }

    private float hypsometricAltitude(float temperature, float seaLevelPressure, float ourPressure) {
        return (float) ((Math.pow((seaLevelPressure / ourPressure), (1 / 5.257d)) - 1) * (temperature + 273.15) / 0.0065f);
    }

    private void detectSteps(long timestamp) {

        if (timestamp - lastStepTimestamp < MIN_STEP_TIME_LENGTH) { //TODO zvětšit length
            return;
        }

        float xG = Math.abs(valuesGr[modulo(pGr - 3, SIZE)]);
        float yG = Math.abs(valuesGr[modulo(pGr - 2, SIZE)]);
        float zG = Math.abs(valuesGr[modulo(pGr - 1, SIZE)]);

        if (xG > yG && xG > zG) { //telefon na boku nepoužíváme
            return;
        }

        //float xA = valuesA[modulo(pA - 3, SIZE)];
        float yA = valuesA[modulo(pA - 2, SIZE)];
        float zA = valuesA[modulo(pA - 1, SIZE)];
        float totalG = /*xG + */yG + zG;

        //xG /= totalG;
        yG /= totalG;
        zG /= totalG;
        float groundFlow = yA * yG + zA * zG;

        //není-li pohyb na negravitačních osách signifikántní, nedetekujeme krok

        if (groundFlow > STEP_GRAVITY_DRIFT && state == 0) {
            state = 1;
        } else if (groundFlow < 0 && state == 1) {
            state = -1;
        } else if (groundFlow > 0 && state == -1) {
            handleMovementEpisode(timestamp);
            state = 0;
        }

    }

    /**
     * @param a your number
     * @param b your modulo number
     * @return real modulo
     */
    private int modulo(int a, int b) {
        return (a % b + b) % b;
    }

    /**
     * Called by single step or by filling up arguments
     */
    private void handleMovementEpisode(long timestamp) {

        //synchronizace hodnot
        if (pR / 4 < pGr / 3) { //pokud je méně pR hodnot, tak pGr snížíme
            pGr += ((pR / 4 - pGr / 3) * 3);
        }

        float[] dist = new float[]{0f, 0f, 0f}; //osy telefonu
        float[] realDist = new float[]{0f, 0f, 0f}; //osy světa
        int count = pGr / 3;

        float gravityMax = 0;
        float gravityMin = 0;
        float nonGravityMax = 0;
        float nonGravityMin = 0;

        float gravityFlow = 0;
        float nonGravityFlow = 0;

        //vynechání výpočtu pohybu, pokud je nějaký gyroskop rychlý - tzn v podstatě eliminace odstředivé síly <3
        decreaseAccordingToCenteredForce(); //řeší odstředivku

        float length = STEP_LENGTH;

        for (int i = 0; i < pGr; i += 3) { //&& (i/3*4) < pR toto by šlo eventuelně

            //float gX = (valuesGr[i]);
            float gY = (valuesGr[i + 1]);
            float gZ = (valuesGr[i + 2]);

            //Tak, todo zde přidat hodnoty z akcelerometru <3

            //normalizace
            float total = /*Math.abs(gX) + */Math.abs(gY) + Math.abs(gZ);
            //gX /= total;
            gY /= total;
            gZ /= total;

            //TODO takže vyřadit gyrosig - to se totiž vracet nebude
            gravityFlow = ((gY) * valuesA[i + 1]) + ((gZ) * valuesA[i + 2]);
            nonGravityFlow = ((1 - gY) * valuesA[i + 1]) + ((1 - gZ) * valuesA[i + 2]);

            gravityMax = gravityMax < gravityFlow ? gravityFlow : gravityMax;
            gravityMin = gravityMin > gravityFlow ? gravityFlow : gravityMin;
            nonGravityMax = nonGravityMax < nonGravityFlow ? nonGravityFlow : nonGravityMax;
            nonGravityMin = nonGravityMin > nonGravityFlow ? nonGravityFlow : nonGravityMin;

            dist[1] += (gZ / count);
            dist[2] -= (gY / count); //do mínus zetu se pohybujeme <3
            //odkaz na osy https://developer.android.com/reference/android/hardware/SensorEvent.html

            //pohyb telefonu nás v podstatě zajímá jen dopředu dozadu
            //pokud je telefon patou dolu, pak jakoby dolu nahoru po Z
            int iR = i / 3 * 4;
            float[] realWorldMove = getRealWorldMove(dist, valuesR[iR], valuesR[iR + 1], valuesR[iR + 2], valuesR[iR + 3]);
            add(realDist, realWorldMove);
        }

        //ořez
        //gravityMax = Math.min(gravityMax, 10);
        //gravityMin = Math.max(gravityMin, 4);

        float nonGravityDiff = nonGravityMax - nonGravityMin;
        float gravityDiff = Math.min(Math.max((gravityMax - gravityMin), 4), 10);
        if (nonGravityDiff > SIG_WALK) { //hodnoty maxima a minima po negravitační ose jsou dostatečné pro krok

            length = (-1.91f * gravityDiff * gravityDiff) + (34.1f * gravityDiff) - 65f;

            System.out.println(String.format("Délka kroku: %s, gravityDiff: %s, nonGravityDiff: %s", length, gravityDiff, nonGravityDiff));

            //todo bacha světové souřadnice mají Z gravitační - proto se používá [0] a [1] jako x y - just for info
            float total = Math.abs(realDist[0]) + Math.abs(realDist[1]) + Math.abs(realDist[2]);
            realDist[0] /= total;
            realDist[1] /= total;
            realDist[2] /= total;

            //onPositionDetected("Na východ " + realDist[0] * length + ", Na sever " + realDist[1] * length);
            for (LocationListener locationListener : listeners) {
                double newLat = this.nowPos.getLat() + realDist[0] * length / 10;
                double newLng = this.nowPos.getLng() + realDist[1] * length / 10;
                nowPos = new MapUtils.LatLng(newLat, newLng);
                locationListener.onPositionDetected(nowPos);
            }


        }

        pA = 0;
        pG = 0;
        pGr = 0;
        pR = 0;
        lastStepTimestamp = timestamp;

    }

    /**
     * @param distance          float[3] with X Y Z of phone coordination system
     * @param orientationValues x y z θ from orientation vector
     * @return
     */
    public float[] getRealWorldMove(float[] distance, float... orientationValues) { //float[]{x, y, z, θ]
        float[] rotMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rotMat, orientationValues);

        float[] realPos = new float[3]; // = mat * vec;

        realPos[0] = rotMat[0] * distance[0] + rotMat[1] * distance[1] + rotMat[2] * distance[2];
        realPos[1] = rotMat[3] * distance[0] + rotMat[4] * distance[1] + rotMat[5] * distance[2];
        realPos[2] = rotMat[6] * distance[0] + rotMat[7] * distance[1] + rotMat[8] * distance[2];

        return realPos;
    }

    public void add(float[] base, float[] additions) {
        base[0] += additions[0];
        base[1] += additions[1];
        base[2] += additions[2];
    }

    private void onFloorDetected(int floor) {
        for (LocationListener listener : listeners) {
            listener.onFloorDetected(floor);
        }
    }

    private void onPositionDetected(String raw) {
        for (LocationListener listener : listeners) {
            //listener.onPositionLogged(raw);
            //listener.onPositionDetected(velocity); //todo co bude posílat
        }
    }

    //getters and registrations
    public int getActualFloor() {
        return lastFloor;
    }

    public int getActualPosition() {
        return 0; //todo
    }


    public static void register(LocationListener listener) {
        listeners.add(listener);
    }

    public static void unregister(LocationListener listener) {
        listeners.remove(listener);
    }

}
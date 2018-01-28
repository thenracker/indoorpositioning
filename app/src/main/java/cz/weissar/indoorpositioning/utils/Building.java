package cz.weissar.indoorpositioning.utils;

/**
 * Created by petrw on 17.12.2017.
 */

public enum Building {

    //počet pater, nadmořská výška paty budovy, výšky jednotlivých pater od přízemí výš
    //TODO ty 4 rozměry zapsat jako metry nad mořem při chůzi s telefonem v ruce ;)
    A(4, 50.2042278f, 15.8293336f, 2.93f, 2.82f, 2.82f, 2.78f), //todo
    B(4, 50.2098997f, 15.8262761f, 2.93f, 2.82f, 2.82f, 2.78f), //todo,
    C(4, 50.2094361f, 15.8265978f, 2.93f, 2.82f, 2.82f, 2.78f), //todo
    E(4, 50.2111389f, 15.8496647f, 2.93f, 2.82f, 2.82f, 2.78f), //todo
    J(4, 50.2035653f, 15.8299775f, 1023.2f, 231.809f, 236.232f, 239.59f, 242.99f),
    S(4, 50.2038569f, 15.8283681f, 2.93f, 2.82f, 2.82f, 2.78f); //todo

    int floorCount; //počet poater
    float lat;
    float lng;
    float baseHeight; //nadmořská výška paty přízemí v m. n. m.
    float[] floorAltitudes; //nadmořské výšky pater počínaje prvním (tedy přízemím), ...
    float outPressure;

    Building(int floorCount, float lat, float lng, float outPressure, float... floorAltitudes) {
        this.floorCount = floorCount;
        this.lat = lat;
        this.lng = lng;
        this.baseHeight = baseHeight;
        this.floorAltitudes = floorAltitudes;
        this.outPressure = outPressure;
    }

    public float getFloorForAltitude(float height, float nowOutsidePressure) {
        float delta = (nowOutsidePressure - outPressure) / 7;
        height += (delta * 1.745875); //s rostoucím tlakem driftuje výpočet výšky
        //plus výška člověka a telefon u pasu  + 0.73f ??

        int indexOfMinimum = 0;
        float maxDif = Float.MAX_VALUE;
        for (int i = 0; i < floorAltitudes.length; i++) {
            float dif = Math.abs(height - floorAltitudes[i]);
            if (dif < maxDif){
                indexOfMinimum = i;
                maxDif = dif;
            }

        }

        return indexOfMinimum + 1; //od 1 do x pater
    }


    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public MapUtils.LatLng getLatLng(){
        return new MapUtils.LatLng(lat, lng);
    }

    public MapUtils.LatLng[] possibleStairs() {
        return new MapUtils.LatLng[]{}; //TODO
    }

}

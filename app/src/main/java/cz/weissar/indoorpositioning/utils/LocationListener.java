package cz.weissar.indoorpositioning.utils;


/**
 * Created by petrw on 11.11.2017.
 */

public interface LocationListener {

    void onFloorDetected(int floor);

    void onPositionDetected(MapUtils.LatLng newPosition);

    void onValuesMeasured(String... measuredValues); //insert values you want

}

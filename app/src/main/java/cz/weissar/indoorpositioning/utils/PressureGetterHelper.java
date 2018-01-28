package cz.weissar.indoorpositioning.utils;

/**
 * Created by petrw on 09.12.2017.
 */

public class PressureGetterHelper {

    private static final String HPA = "hPa";

    public interface Callback {
        void response(float pressureAtSeaLevel, float temperature);
    }

    public static void process(String raw, Callback callback) throws Exception {
        if (raw.contains(HPA)) {
            String[] split = raw.split(HPA);
            split[0] = split[0].trim();
            String[] split2 = split[0].split("<b>");

            String[] split3 = split[0].split("°C");
            split3[0] = split3[0].trim();
            String[] split4 = split3[0].split("<b>");

            float pressure = Float.valueOf(split2[split2.length - 1]);
            float temperature = 0; //Float.valueOf(split4[split4.length - 1]);

            // 232 výška stanice

            float pressureSea = pressure; //hypsometricFormula(232, pressure, temperature); //ono už to na serveru je správně přepočteno <3

            callback.response(pressureSea, temperature);
        }

    }


    /**
     * @return pressure on a sea level
     */
    /*private static float hypsometricFormula(float heightAtP, float pressureP, float temperature) {
        //https://physics.stackexchange.com/questions/333475/how-to-calculate-altitude-from-current-temperature-and-pressure
        // h = (((P0/P)^(1/5.257) - 1) * (T + 273,15))/0.0065
        temperature += 273.15f;
        float randal = 1f + (heightAtP * 0.0065f / temperature);
        float pressureP0 = pressureP * (float) Math.pow((double) randal, 5.257d);
        return pressureP0;
    }*/

}

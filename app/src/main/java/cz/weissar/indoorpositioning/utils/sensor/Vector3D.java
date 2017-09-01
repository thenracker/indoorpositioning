package cz.weissar.indoorpositioning.utils.sensor;

/**
 * Created by petrw on 12.06.2017.
 */

public class Vector3D {

    private int type;
    private float[] values;

    public static Vector3D newInstance(float initX, float initY, float initZ, int type) {
        Vector3D vec = new Vector3D();
        vec.values = new float[]{initX, initY, initZ};
        vec.type = type;
        return vec;
    }

    public void newValues(float newX, float newY, float newZ) {
        values[0] = newX;
        values[1] = newY;
        values[2] = newZ;
    }

    public float[] getValues() {
        return values;
    }

    public float getX() {
        return values[0];
    }

    public float getY() {
        return values[1];
    }

    public float getZ() {
        return values[2];
    }

    public int getType() {
        return type;
    }
}

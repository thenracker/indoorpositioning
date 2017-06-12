package cz.weissar.indoorpositioning.utils.sensor;

/**
 * Created by petrw on 12.06.2017.
 */

public class Vector3D {

    int type;
    float xOld, yOld, zOld;
    float deltaX, deltaY, deltaZ;

    public static Vector3D newInstance(float initX, float initY, float initZ, int type) {
        Vector3D vec = new Vector3D();
        vec.xOld = initX;
        vec.yOld = initY;
        vec.zOld = initZ;
        vec.type = type;
        return vec;
    }

    public void newValues(float newX, float newY, float newZ){
        this.deltaX += (newX - xOld);
        this.deltaY += (newY - yOld);
        this.deltaZ += (newZ - zOld);
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public float getDeltaZ() {
        return deltaZ;
    }

    public int getType() {
        return type;
    }
}

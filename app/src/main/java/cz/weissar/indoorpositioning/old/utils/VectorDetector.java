//package cz.weissar.indoorpositioning.old.utils;
//
//import cz.weissar.indoorpositioning.old.transforms.Quat;
//import cz.weissar.indoorpositioning.old.transforms.Vec3D;
//
///**
// * Created by petrw on 10.09.2017.
// */
//
//public class VectorDetector {
//
//    float[] xS, yS, zS; //10 fields at least
//    int pointer;
//    float[] dirVec;
//    Quat quaternion;
//    long lastTimestamp;
//
//    final static int SIZE = 10;
//
//    //final static float
//
//    public VectorDetector() {
//        xS = new float[10];
//        yS = new float[SIZE];
//        zS = new float[SIZE];
//        dirVec = new float[]{0, 0, 0};
//        pointer = 0; //initial position
//    }
//
//    public void addNewVals(float x, float y, float z, long timestamp) {
//        //save to history
//        xS[pointer] = x;
//        yS[pointer] = y;
//        zS[pointer] = z;
//        pointer++;
//        pointer %= SIZE;
//
//        //3 - 10 ms
//        //accelerate dirVec and dont forget the old vector stops a bit
//        /*long dif = timestamp-lastTimestamp;
//        dirVec[0]/=dif;
//        dirVec[0]+=x;
//        dirVec[1]/=dif;
//        dirVec[1]+=y;
//        dirVec[2]/=dif;
//        dirVec[2]+=z;*/
//
//        //Log.d("GYRO X Y Z ms", String.format("%.5f, %.5f, %.5f, %s ms", x, y, z, timestamp));
//
//        //and finally mul by quaternion?
//
//        lastTimestamp = timestamp;
//    }
//
//    private Vec3D mulByQuaternion(Quat q, float[] vec){
//        return new Vec3D(vec[0], vec[1], vec[2]).mul(q);
//    }
//
//    /**
//     * @return vector of last 10 vals with appropriate esteem
//     * The newest vals are 100% actual, the second one is 95%, third 90%, and so on...
//     */
//    public float[] getEsteemedVec() {
//        if (xS[9] == 0.0f) return dirVec; //zatim nejsou hodnoty, takže dirVec bez výpočtů
//
//        float[] esteemedVec = new float[3];
//
//        esteemedVec[0] = esteem10_5_3_2_1(xS);
//        esteemedVec[1] = esteem10_5_3_2_1(yS);
//        esteemedVec[2] = esteem10_5_3_2_1(zS);
//
//        return esteemedVec;
//    }
//
//    public float[] getPureVec(){
//        return new float[]{xS[pointer], yS[pointer], zS[pointer]};
//    }
//
//    private float esteem10_9_8_7_6_5_4_3_2_1(float[] s) {
//        int i = pointer;
//        return (SIZE * s[i] + 9 * s[mod(i - 1)] + 8 * s[mod(i - 2)] + 7 * s[mod(i - 3)] + 6 * s[mod(i - 4)] + 5 * s[mod(i - 5)] + 4 * s[mod(i - 6)] + 3 * s[mod(i - 7)] + 2 * s[mod(i - 8)] + s[mod(i - 9)]) / 55;
//    }
//
//    private float esteem10_5_3_2_1(float[] s) {
//        int i = pointer;
//        return (SIZE * s[i] + 5 * s[mod(i - 1)] + 3 * s[mod(i - 2)] + 2 * s[mod(i - 3)] + 1 * s[mod(i - 4)]) / 21;
//    }
//
//    private int mod(int x) {
//        return mod(x, SIZE);
//    }
//
//    private int mod(int x, int modulo) {
//        return ((x % modulo) + modulo) % modulo;
//    }
//
//    public void setQuaternion(Quat quaternion) {
//        this.quaternion = quaternion;
//    }
//}

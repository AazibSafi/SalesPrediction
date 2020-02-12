package sajid.bussinesssale.Prediction;

/**
 * Created by aazib on 12-Jul-17.
 */

public class Normalization {

    private static double DAXMAX = 10000.0D;

    public static double[] normalizeValues(double[] data) {
        for(int i=0;i<data.length;i++) {
            data[i] = data[i] / DAXMAX;
        }
        return data;
    }

    public static double[] deNormalizeValue(double[] data) {
        for(int i=0;i<data.length;i++) {
            data[i] = data[i] * DAXMAX;
        }
        return data;
    }
}

package sajid.bussinesssale.Prediction;

import android.util.Log;

/**
 * Created by aazib on 14-Jul-17.
 */

public class NeuralNetworkController {

    public double[] doPrediction(double[] rawTrainingData) {
        double[] output;

        int windowsSize = calculateSlidingWindowSize(rawTrainingData);

        if(windowsSize != -1) {

            double[] testData = getTestData(windowsSize,rawTrainingData);

            NeuralNetworkPrediction artificialNeuralNetwork = new NeuralNetworkPrediction(windowsSize);

            artificialNeuralNetwork.trainNetwork(
                    Normalization.normalizeValues(rawTrainingData));

            output = artificialNeuralNetwork.predictNext(
                    Normalization.normalizeValues(testData));

            output = Normalization.deNormalizeValue(output);
        }
        else {
            output = new double[]{};
            Log.e("TrainingData","inSufficient Data Provided");
        }

        return output;
    }

    public double[] getTestData(int windowsSize,double[] rawTrainingData) {
        double[] testData = new double[windowsSize];

        for (int i = 0; i < windowsSize; i++) {
            testData[windowsSize-i-1] = rawTrainingData[rawTrainingData.length - i - 1];
        }

        return testData;
    }

    public int calculateSlidingWindowSize(double[] data) {
        int normalSize = 4;

        if(data == null || data.length == 0 )
            return -1;

        return (data.length > normalSize) ? normalSize : (data.length-1);
    }
}

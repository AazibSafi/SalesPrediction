package sajid.bussinesssale.Prediction;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;

import sajid.bussinesssale.Helper.Utils;

/*
 * @author Aazib Safi Patoli
 */

public class NeuralNetworkPrediction {

    private double MAX_ERROR = 0.001;
    private double LEARNING_RATE = 0.7;
    private int MAX_ITERATIONS = 1000;

    private int SLIDING_WINDOW_SIZE;

    private NeuralNetwork neuralNetwork;

    NeuralNetworkPrediction(int windowSize) {
        SLIDING_WINDOW_SIZE = windowSize;

        neuralNetwork = new MultiLayerPerceptron(SLIDING_WINDOW_SIZE, 2*SLIDING_WINDOW_SIZE, 1);

        ((LMS) neuralNetwork.getLearningRule()).setMaxError(MAX_ERROR);//0-1
        ((LMS) neuralNetwork.getLearningRule()).setLearningRate(LEARNING_RATE);//0-1
        ((LMS) neuralNetwork.getLearningRule()).setMaxIterations(MAX_ITERATIONS);//0-1
    }

    public void trainNetwork(double[] trainingData) {
        TrainingSet trainingSet = new TrainingSet();

        int j=0;

        for(int i=0;(i+j) < trainingData.length;i++) {

            double[] inputValues = new double[SLIDING_WINDOW_SIZE];
            double[] outputValue = new double[1];

            j=0;
            while(j<SLIDING_WINDOW_SIZE) {
                System.out.println("J: "+j+"\ti+j: "+(i+j));
                inputValues[j] = trainingData[i+j];
                j++;
            }
            outputValue[0] = trainingData[i+j];

            trainingSet.addElement(new SupervisedTrainingElement(inputValues, outputValue));

//            Utils.printValues(inputValues,"inputValues");
//            Utils.printValues(outputValue,"outputValue");
        }

        neuralNetwork.learnInSameThread(trainingSet);
    }

    public double[] predictNext(double[] testData) {

//        Utils.printValues(testData,"predictNext TestData");

        TrainingSet testSet = new TrainingSet();
        testSet.addElement(new TrainingElement(testData));

        neuralNetwork.setInput(testSet.trainingElements().get(0).getInput());
        neuralNetwork.calculate();
        double[] networkOutput = neuralNetwork.getOutputAsArray();

//        Utils.printValues(testData,"Input");
//        Utils.printValues(networkOutput,"Output");

        return networkOutput;
    }
}

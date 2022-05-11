package Sandbox;

import java.awt.*;
import java.util.Random;

public class Functions {

    static Random rand = new Random();

    //Rosenblatt's Perceptron Learning  Rule
    public static double PerceptronLearningRule (double learningRate, double desiredOutput, double actualOutput, double input) {
        //Apply perceptron learning rule
        double adjustedWeight = learningRate*(desiredOutput-actualOutput)*input;
        //Round the adjusted weight to 2 decimal places. This just makes it easier to follow.
        return Math.round(adjustedWeight * 100.0) / 100.0;
    }

    //--------------
    //Cost functions
    //--------------

    public static double MeanSquaredError (float[][] values) {
        double sum = 0;

        for (int row = 0; row < (values[0].length) ; row++) {
            sum = sum + Math.pow((values[0][row]-values[1][row]),2);
            System.out.println((values[0][row]+", "+(values[1][row])));
        }
        int n = values.length-1;
        double cost = sum / n;
        return Math.round(cost * 100.0) / 100.0;
    }

    public static double MeanAbsoluteError (float[][] values) {
        double sum = 0;
        for (int row = 0 ; row < values[0].length ; row++) {
            System.out.println("doing row "+row);
            sum = sum + (values[1][row]-values[0][row]);
        }
        int n = values.length-1;
        double cost = sum / n;
        return Math.round(cost * 100.0) / 100.0;
    }

    //--------------------
    //Activation functions
    //--------------------

    public static float HeavisideStep (double z) {
        int out;
        if (z < 0){
            out = 0;
        } else {
            out = 1;
        }
        return out;
    }

    public static double Sigmoid (double x) {
        double z = 1 / (1 + Math.exp(-x));
        //Round the output to 2 decimal places
        return (Math.round(z * 100.0) / 100.0);
    }

    public static double ReLu (double x) {
        if (x < 0) {
            x = 0;
        }
        return x;
    }

    //------------------------
    //Initialisation Techniques
    //------------------------

    public static double initialiseRandom () {
        float z = -1 + rand.nextFloat() * (2);
        //Round to 2 decimal places and return
        return Math.round((double) z * 100.0) / 100.0;
    }

    public static double initialiseXavier (double numInputs, double numOutputs) {
        //Find suitable deviation based on number of inputs and outputs
        double a = Math.sqrt(2/(numInputs+numOutputs));
        //Generate a value based on this distribution with a mean of 0
        double z = rand.nextGaussian()*a;
        //Round to 2 decimal places and return
        return Math.round(z * 100.0) / 100.0;
    }

    //------------------------
    //        Other
    //------------------------

    //This is the Sigmoid activation which has been differentiated. Used for computing the derivative chain as part of backpropagation.
    public static double sigmoidDifferential (double x) {
        return 1 / (1 + Math.exp(-x)) * (1-(1 / (1 + Math.exp(-x))));
    }

    //This is the ReLu activation which has been differentiated. Used for computing the derivative chain as part of backpropagation.
    public static double ReLuDifferential (double x) {
        double z = 0;
        if (x >= 0) {
            z = 1;
        }
       return z;
    }

    //MSE function which has been differentiated. Used for computing the derivative chain as part of backpropagation.
    public static double MSEDifferential (double prediction, double actual) {
        return 2*(prediction-actual);
    }

    //MAE function which has been differentiated. Used for computing the derivative chain as part of backpropagation.
    public static double MAEDifferential (double prediction, double actual) {
        if (prediction > actual) {
            return 1;
        } else {
            return -1;
        }
    }

    //This function will return the hex value of a suitable colour for edges, if 'Colour by Weight' setting is turned on
    public static Color edgeColour (double weight) {
        int r,g,b;

        //weight = -weight;

        r = (int) (160.0f * -weight);
        g = (int) (90.0f * (1 - (-weight)))-60;
        b = 15;

        //Check to ensure the RGB values do not go over max value (255)
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }

        return new Color(r,g,b);
    }


}

package Sandbox;

import Interface.MainWindow;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static Interface.MainWindow.*;

public class SimpleNetworkController {

    public static int step = 0;
    public static int stage = 0;

    //This will be used to recall if the network has been initialised before (so it is not done again)
    public static boolean hasBeenInitialised = false;

    //Inputs and Outputs
    public static double input1 = 0;
    public static double input2 = 0;
    public static double output = 0;

    //Initial weights
    public static double initWeight1 = 0;
    public static double initWeight2 = 0;
    public static double initWeight3 = 0;
    public static double initWeight4 = 0;
    public static double initWeight5 = 0;
    public static double initWeight6 = 0;

    public static double initWeightOut1 = 0;
    public static double initWeightOut2 = 0;
    public static double initWeightOut3 = 0;

    //Weights to display
    public static double weight1 = 0f;
    public static double weight2 = 0f;
    public static double weight3 = 0f;
    public static double weight4 = 0f;
    public static double weight5 = 0f;
    public static double weight6 = 0f;

    //Output weights (neurons to output)
    public static double weightOut1 = 0f;
    public static double weightOut2 = 0f;
    public static double weightOut3 = 0f;

    //Neuron Sums
    //'z' value of all summed inputs to a neuron
    public static double sum1; //z top neuron
    public static double sum2; //z middle neuron
    public static double sum3; //z bottom neuron
    public static double sum4; //z output neuron
    //'a' value for the activation output
    public static double function1; //a top neuron
    public static double function2; //a middle neuron
    public static double function3; //a bottom neuron

    //RetentionWeights
    //These will be used to remember the previous weight prior to the most recent adjustment
    public static double oldWeight1 = 0f;
    public static double oldWeight2 = 0f;
    public static double oldWeight3 = 0f;
    public static double oldWeight4 = 0f;
    public static double oldWeight5 = 0f;
    public static double oldWeight6 = 0f;

    public static double oldWeightOut1 = 0f;
    public static double oldWeightOut2 = 0f;
    public static double oldWeightOut3 = 0f;

    //Sample row
    public static int row = 0;
    //Data
    public static List<List<String>> data = DataFeed.data(MainWindow.dataSetFile);

    //Output
    public static double out = 0;
    //Cost
    public static float cost;

    //Chain (for backpropagation)
    public static double chain;

    //Text to display as explanation
    public static String tip = "A simple neural network. We have 2 inputs, a hidden layer with 3 neurons, and an output";
    //Text to store as log entry
    public static String log = "Error! No log entry to display";

    public static void reset() {

        try {
            MainWindow.displayTray("Network Reset","The network has been reset to its original state");
        } catch (AWTException e) {
            e.printStackTrace();
        }

        //This function will reset any important values to their original states
        weight1 = 0f;
        weight2 = 0f;
        weight3 = 0f;
        weight4 = 0f;
        weight5 = 0f;
        weight6 = 0f;
        weightOut1 = 0f;
        weightOut2 = 0f;
        weightOut3 = 0f;
        function1 = 0f;
        function2 = 0f;
        function3 = 0f;
        sum1 = 0f;
        sum2 = 0f;
        sum3 = 0f;
        sum4 = 0f;
        out = 0;

        row = 0;

        hasBeenInitialised = false;

        currentSample = 0;

        step = 0;
        stage = 0;

        MainWindow.sampleCount = data.size();

        tip = "A simple neural network. We have 2 inputs, a hidden layer with 3 neurons, and an output";
        log = ""; //This is an empty action, so create no log entry
    }

    //The chain for finding a weight with respect to cost in the first layer (connecting inputs to neurons)
    public static double firstLayerChain (double outputOfTarget, double secondWeight, double z1, double z2, double predicted, double actual) {
        double chain = 0;

        if (Objects.equals(activation, "Sigmoid") && Objects.equals(costMetric, "Mean Squared Error")) {
            chain = outputOfTarget*Functions.sigmoidDifferential(z1)*secondWeight*Functions.sigmoidDifferential(z2)*Functions.MSEDifferential(predicted,actual);
        } else if (Objects.equals(activation, "Sigmoid") && Objects.equals(costMetric, "Mean Absolute Error")) {
            chain = outputOfTarget*Functions.sigmoidDifferential(z1)*secondWeight*Functions.sigmoidDifferential(z2)*Functions.MAEDifferential(predicted,actual);
        } else if (Objects.equals(activation, "ReLu") && Objects.equals(costMetric, "Mean Squared Error")) {
            chain = outputOfTarget*Functions.ReLuDifferential(z1)*secondWeight*Functions.sigmoidDifferential(z2)*Functions.MSEDifferential(predicted,actual);
        } else if (Objects.equals(activation, "ReLu") && Objects.equals(costMetric, "Mean Absolute Error")) {
            chain = outputOfTarget*Functions.ReLuDifferential(z1)*secondWeight*Functions.sigmoidDifferential(z2)*Functions.MAEDifferential(predicted,actual);
        }

        return chain;
    }

    //The chain for finding a weight with respect to cost in the second layer (connecting neurons to output)
    //(It may seem strange there is no accounting for activation here, but the second layer weights do not pass through an activation before cost is computed)
    public static double secondLayerChain (double sum, double out, double target) {
        double chain = 0;

        if (Objects.equals(activation, "Sigmoid") && Objects.equals(costMetric, "Mean Squared Error")) {
            chain = out*Functions.sigmoidDifferential(sum)*Functions.MSEDifferential(out,target);
        } else if (Objects.equals(activation, "Sigmoid") && Objects.equals(costMetric, "Mean Absolute Error")) {
            chain = out*Functions.sigmoidDifferential(sum)*Functions.MAEDifferential(out,target);
        } else if (Objects.equals(activation, "ReLu") && Objects.equals(costMetric, "Mean Squared Error")) {
            chain = out*Functions.ReLuDifferential(sum)*Functions.MSEDifferential(out,target);
        } else if (Objects.equals(activation, "ReLu") && Objects.equals(costMetric, "Mean Absolute Error")) {
            chain = out*Functions.ReLuDifferential(sum)*Functions.MAEDifferential(out,target);
        }

        return chain;
    }

    public static void previousStep() {
        //The user wishes to do the previous step

        if (step == 3) {
            //The user is on step 3 (summation) and clicked back
            //Reset the sum value
            sum1 = 0;
        } else if (step == 4) {
            //The user is on step 4 (summation) and clicked back
            //Reset the sum value
            sum2 = 0;
        } else if (step == 5) {
            //The user is on step 5 (summation) and clicked back
            //Reset the sum value
            sum3 = 0;
        } else if (step == 6) {
            //The user is on step 4 (activation) and clicked back
            //Reset the function value
            function1 = 0;
        } else if (step == 7) {
            //The user is on step 4 (activation) and clicked back
            //Reset the function value
            function2 = 0;
        } else if (step == 8) {
            //The user is on step 4 (activation) and clicked back
            //Reset the function value
            function3 = 0;
        }

        if (step > 1) {
            step = step -1;
            doStep();
            System.out.println("Moving from step "+(step+1)+" to "+step);
        } else if (step == 1) {
            step = 0;
            weight1 = 0f;
            weight2 = 0f;
            weight3 = 0f;
            weight4 = 0f;
            weight5 = 0f;
            weight6 = 0f;
            weightOut1 = 0f;
            weightOut2 = 0f;
            weightOut3 = 0f;
            tip = "A simple neural network. We have 2 inputs, a hidden layer with 3 neurons, and an output";
            System.out.println("Moving from step "+(step+1)+" to "+step);

        } else {
            System.out.println("Already on step 0");
        }

    }

    public static void nextStep() {
        //The user wishes to do the next step

        //Check if the dataset is finished with (i.e. there are no more steps)
        if (!MainWindow.dataComplete) {
            System.out.println("Moving from step "+(step)+" to "+(step+1));
            step++;
            doStep();
        }

    }

    public static void doStep() {

        //Initialise this for later reference
        double z;
        if (!MainWindow.testing) {

        switch (step) {
            case 0:
                //Default state (not pressed forward yet)
                stage = 0;
                weight1 = 0f;
                weight2 = 0f;
                weight3 = 0f;
                weight4 = 0f;
                weight5 = 0f;
                weight6 = 0f;
                weightOut1 = 0f;
                weightOut2 = 0f;
                weightOut3 = 0f;
                out = 0;

                //Get number of rows of the loaded dataset that are being worked with
                //This is used for the progress bar
                MainWindow.sampleCount = data.size();

                tip = "A simple neural network. We have 2 inputs, a hidden layer with 3 neurons, and an output";
                log = ""; //This is an empty action, so create no log entry
                break;
            case 1:
                stage = 1;
                //initialise weights

                //Reset the input nodes in case the user steps backwards
                input1 = 0f;
                input2 = 0f;

                if (!hasBeenInitialised) {
                    if (Objects.equals(initialisation, "Random")) {
                        weight1 = Functions.initialiseRandom();
                        weight2 = Functions.initialiseRandom();
                        weight3 = Functions.initialiseRandom();
                        weight4 = Functions.initialiseRandom();
                        weight5 = Functions.initialiseRandom();
                        weight6 = Functions.initialiseRandom();
                        weightOut1 = Functions.initialiseRandom();
                        weightOut2 = Functions.initialiseRandom();
                        weightOut3 = Functions.initialiseRandom();
                        //Set initial weights (in case user steps back)
                        initWeight1 = weight1;
                        initWeight2 = weight2;
                        initWeight3 = weight3;
                        initWeight4 = weight4;
                        initWeight5 = weight5;
                        initWeight6 = weight6;
                        initWeightOut1 = weightOut1;
                        initWeightOut2 = weightOut2;
                        initWeightOut3 = weightOut3;
                        hasBeenInitialised = true;
                    } else if (Objects.equals(initialisation, "Xavier")) {
                        weight1 = Functions.initialiseXavier(2,1);
                        weight2 = Functions.initialiseXavier(2,1);
                        weight3 = Functions.initialiseXavier(2,1);
                        weight4 = Functions.initialiseXavier(2,1);
                        weight5 = Functions.initialiseXavier(2,1);
                        weight6 = Functions.initialiseXavier(2,1);
                        weightOut1 = Functions.initialiseXavier(2,1);
                        weightOut2 = Functions.initialiseXavier(2,1);
                        weightOut3 = Functions.initialiseXavier(2,1);
                        //Set initial weights (in case user steps back)
                        initWeight1 = weight1;
                        initWeight2 = weight2;
                        initWeight3 = weight3;
                        initWeight4 = weight4;
                        initWeight5 = weight5;
                        initWeight6 = weight6;
                        initWeightOut1 = weightOut1;
                        initWeightOut2 = weightOut2;
                        initWeightOut3 = weightOut3;
                        hasBeenInitialised = true;
                    }
                } else {
                    //The network has been initialised previously, so simply set weights to their original initialised values
                    weight1 = initWeight1;
                    weight2 = initWeight2;
                    weight3 = initWeight3;
                    weight4 = initWeight4;
                    weight5 = initWeight5;
                    weight6 = initWeight6;
                    weightOut1 = initWeightOut1;
                    weightOut2 = initWeightOut2;
                    weightOut3 = initWeightOut3;
                }

                out = 0;
                if (Objects.equals(initialisation, "Random")) {
                    tip = "Initialise the weights randomly with values between -1 and 1";
                } else if (Objects.equals(initialisation, "Xavier")){
                    tip = "Initialise the weights randomly in a uniform distribution (Xavier)";
                }
                log = "Initialise Weights ("+MainWindow.initialisation+")";
                break;
            case 2:
                stage = 2;

                //Get the next row of the dataset
                List sample = data.get(row);

                input1 = Double.parseDouble((String) sample.get(0));
                input2 = Double.parseDouble((String) sample.get(1));
                output = Double.parseDouble((String) sample.get(2));

                tip = "We get the next sample from the dataset. Our desired output is "+output;
                log = "Retrieve next sample";
                break;
            case 3:
                stage = 3;
                //Get sum
                z = (weight1*input1) + (weight4*input2);
                sum1 = z;
                tip = "We sum the inputs multiplied by their weights, giving us a result of "+(Math.round(sum1 * 100.0) / 100.0);
                log = "Get sum of inputs (Top neuron)";
                break;
            case 4:
                stage = 4;
                //Get sum
                z = (weight2*input1) + (weight5*input2);
                sum2 = z;
                tip = "We sum the inputs multiplied by their weights, giving us a result of "+(Math.round(sum2 * 100.0) / 100.0);
                log = "Get sum of inputs (Middle neuron)";
                break;
            case 5:
                stage = 5;
                //Get sum
                z = (weight3*input1) + (weight6*input2);
                sum3 = z;
                tip = "We sum the inputs multiplied by their weights, giving us a result of "+(Math.round(sum3 * 100.0) / 100.0);
                log = "Get sum of inputs (Bottom neuron)";
                break;
            case 6:
                stage = 6;
                if (Objects.equals(activation, "Sigmoid")) {
                    function1 = Functions.Sigmoid(sum1);
                } else if (Objects.equals(activation, "ReLu")) {
                    function1 = Functions.ReLu(sum1);
                }
                tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum1 * 100.0) / 100.0)+") = "+function1;
                log = "Apply activation function ("+MainWindow.activation+")";
                break;
            case 7:
                stage = 7;
                if (Objects.equals(activation, "Sigmoid")) {
                    function2 = Functions.Sigmoid(sum2);
                } else if (Objects.equals(activation, "ReLu")) {
                    function2 = Functions.ReLu(sum2);
                }
                tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum2 * 100.0) / 100.0)+") = "+function2;
                log = "Apply activation function ("+MainWindow.activation+")";
                break;
            case 8:
                stage = 8;
                if (Objects.equals(activation, "Sigmoid")) {
                    function3 = Functions.Sigmoid(sum3);
                } else if (Objects.equals(activation, "ReLu")) {
                    function3 = Functions.ReLu(sum3);
                }
                tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum3 * 100.0) / 100.0)+") = "+function3;
                log = "Apply activation function ("+MainWindow.activation+")";
                break;
            case 9:
                stage = 9;

                sum4 = Math.round(((function1*weightOut1) + (function2*weightOut2) + (function3*weightOut3)) * 100.0) / 100.0;
                tip = "Now we can sum our activated values multiplied by our weights to get the network output";
                log = "Sum outputs";
                break;
            case 10:
                stage = 10;
                if (Objects.equals(activation, "Sigmoid")) {
                    out = Functions.Sigmoid(sum4);
                } else if (Objects.equals(activation, "ReLu")) {
                    out = Functions.Sigmoid(sum4);
                }
                tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum4 * 100.0) / 100.0)+") = "+out;
                log = "Apply activation function ("+MainWindow.activation+")";
                break;
            case 11:
                stage = 11;
                sample = data.get(row);

                //Get error and add to performance graph
                float[][] error = {{Float.parseFloat(sample.get(2).toString())},{(float) out}};
                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    cost = (float) Functions.MeanSquaredError(error);
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    cost = (float) Functions.MeanAbsoluteError(error);
                }

                System.out.println("Error was: "+cost+". Adding point.");
                //Add new error point to be plotted
                MainWindow.trainPoints.add(MainWindow.trainPoints.size(),cost);
                //Now refresh dataset so it can be displayed
                MainWindow.dataset = MainWindow.createDataset(trainPoints,testPoints);

                if (out == Float.parseFloat(sample.get(2).toString())) {
                    tip = "Our prediction is correct, so no weight adjustments are required.";
                    step = 30;
                } else {
                    if (Objects.equals(costMetric, "Mean Squared Error")) {
                        tip = "We can see our prediction was Incorrect! The cost with MSE is "+cost;
                    } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                        tip = "We can see our prediction was Incorrect! The cost with MAE is "+cost;
                    }

                }

                //Take a snapshot of the current weights, for in case the user wishes to go backwards after weight adjustments are made.
                oldWeight1 = weight1;
                oldWeight2 = weight2;
                oldWeight3 = weight3;
                oldWeight4 = weight4;
                oldWeight5 = weight5;
                oldWeight6 = weight6;
                oldWeightOut1 = weightOut1;
                oldWeightOut2 = weightOut2;
                oldWeightOut3 = weightOut3;

                log = "Evaluate Output";

                break;
            case 12:

                stage = 12;

                sample = data.get(row);

                tip = "We can adjust our network by using backpropagation. First we compute the derivative chain";
                log = "";

                chain = firstLayerChain(function1,sum1,weight1,sum4,Double.parseDouble(sample.get(2).toString()),out);
                //before it was chain = firstLayerChain(function1,sum1,weightOut1,sum4,Double.parseDouble(sample.get(2).toString()),out);
                //ie you just have the weight, NO INPUT is passed to this function

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 13:

                if (stage == 14) {
                    //If so, revert weight1 to its value prior to adjustment
                    weight1 = oldWeight1;
                }

                stage = 13;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weight1 = Math.round((weight1-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (w1)";

                break;
            case 14:

                stage = 14;

                sample = data.get(row);

                tip = "We can adjust our network by using backpropagation. First we compute the derivative chain";
                log = "";

                chain = firstLayerChain(function2,sum2,weight2,sum4,Double.parseDouble(sample.get(2).toString()),out);

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 15:

                if (stage == 16) {
                    //If so, revert weight1 to its value prior to adjustment
                    weight2 = oldWeight2;
                }

                stage = 15;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weight2 = Math.round((weight2-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (w2)";

                break;
            case 16:

                stage = 16;

                sample = data.get(row);

                tip = "We can adjust our network by using backpropagation. First we compute the derivative chain";
                log = "";

                chain = firstLayerChain(function3,sum3,weight3,sum4,Double.parseDouble(sample.get(2).toString()),out);

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 17:

                if (stage == 18) {
                    //If so, revert weight1 to its value prior to adjustment
                    weight3 = oldWeight3;
                }

                stage = 17;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weight3 = Math.round((weight3-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (w3)";

                break;
            case 18:

                stage = 18;

                sample = data.get(row);

                tip = "We can adjust our network by using backpropagation. First we compute the derivative chain";
                log = "";

                chain = firstLayerChain(function1,sum1,weight4,sum4,Double.parseDouble(sample.get(2).toString()),out);

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 19:

                if (stage == 20) {
                    //If so, revert weight1 to its value prior to adjustment
                    weight4 = oldWeight4;
                }

                stage = 19;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weight4 = Math.round((weight4-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (w4)";

                break;
            case 20:

                stage = 20;

                sample = data.get(row);

                tip = "We can adjust our network by using backpropagation. First we compute the derivative chain";
                log = "";

                chain = firstLayerChain(function2,sum2,weight5,sum4,Double.parseDouble(sample.get(2).toString()),out);

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 21:

                if (stage == 22) {
                    //If so, revert weight1 to its value prior to adjustment
                    weight5 = oldWeight5;
                }

                stage = 21;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weight5 = Math.round((weight5-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (w5)";

                break;
            case 22:

                stage = 22;

                sample = data.get(row);

                tip = "We can adjust our network by using backpropagation. First we compute the derivative chain";
                log = "";

                chain = firstLayerChain(function3,sum3,weight6,sum4,Double.parseDouble(sample.get(2).toString()),out);

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 23:

                //Check if the user just stepped back from step 21
                if (stage == 24) {
                    //If so, revert weight1 to its value prior to adjustment
                    weight6 = oldWeight6;
                }

                stage = 23;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weight6 = Math.round((weight6-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (w6)";

                break;
            case 24:
                //Now moving on to adjust second layer weights
                stage = 24;

                log = "";
                tip = "To adjust the second layer weights, the chain will be different";

                break;
            case 25:

                stage = 25;

                sample = data.get(row);

                tip = "Once again using backpropagation, we first compute the derivative chain";
                log = "";

                chain = secondLayerChain(function1,out,Double.parseDouble(sample.get(2).toString()));

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 26:

                //Check if the user just stepped back from step 26
                if (stage == 27) {
                    //If so, revert weight1 to its value prior to adjustment
                    weightOut1 = oldWeightOut1;
                }

                stage = 26;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weightOut1 = Math.round((weightOut1-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (output weight 1)";

                break;
            case 27:

                stage = 27;

                sample = data.get(row);

                tip = "Once again using backpropagation, we first compute the derivative chain";
                log = "";

                chain = secondLayerChain(function2,out,Double.parseDouble(sample.get(2).toString()));

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 28:

                //Check if the user just stepped back from step 28
                if (stage == 29) {
                    //If so, revert weight1 to its value prior to adjustment
                    weightOut2 = oldWeightOut2;
                }

                stage = 28;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weightOut2 = Math.round((weightOut2-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (output weight 2)";
                break;
            case 29:

                stage = 29;

                sample = data.get(row);

                tip = "Once again using backpropagation, we first compute the derivative chain";
                log = "";

                chain = secondLayerChain(function3,out,Double.parseDouble(sample.get(2).toString()));

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    log = "Compute Chain Rule (" + MainWindow.activation + ", MAE)";
                }

                break;
            case 30:

                //Check if the user just stepped back from step 30
                if (stage == 31) {
                    //If so, revert weight1 to its value prior to adjustment
                    weightOut3 = oldWeightOut3;
                }

                stage = 30;

                if (Objects.equals(costMetric, "Mean Squared Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MSE)";
                } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                    tip = "Then adjust the weight based on its change with respect to cost (MAE)";
                }

                weightOut3 = Math.round((weightOut3-(MainWindow.learningRate*chain)) * 100.0) / 100.0;

                log = "Apply Chain Rule (output weight 3)";
                break;
            case 31:

                tip = "We're now ready for the next sample. Now repeat from Step 2 with new data!";
                log = "Sample Finished";

                step = 1;
                stage = 1;

                //Append the data table row so that the next sample is retrieved for the next cycle
                row = row+1;
                //Also update the sample number within MainWindow
                MainWindow.currentSample = row;

                //Finally, let's check the next samples are not reserved for testing
                if ((MainWindow.sampleCount-MainWindow.currentSample) == MainWindow.samplesForTesting) {
                    //The remaining samples are reserved for testing

                    //Notify the user the network is switching to test phase
                    try {
                        MainWindow.displayTray("Network Testing Started","The network will now test with the hold-out samples. During this time, no adjustments to weights will be made");
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }

                    //Set the testing phase to begin, so next cycle the network will test rather than train
                    MainWindow.testing = true;
                    log = "Testing Finished!";
                    System.out.println("Switching phase to testing!");
                    //The step will also be reset for the next phase
                    step = 32;
                }

                //Also increment the data label that is displayed so it shows the new sample which is being dealt with
                DataTable.rowOffset = DataTable.rowOffset + 1;

                break;
            default:
                //There should always be a next instruction, if not something has gone wrong
                System.out.println("Error! No new instructions for training found");
        }

        } else {
            //The network is not in the training phase, so instead test with the remaining samples

            switch (step) {
                case 32:
                    stage = 32;
                    //Get sum

                    //Get the next row of the dataset
                    List<String> sample = data.get(row);

                    input1 = Double.parseDouble(sample.get(0));
                    input2 = Double.parseDouble(sample.get(1));
                    output = Double.parseDouble(sample.get(2));

                    tip = "We get the next sample from the dataset. Our desired output is "+output;
                    log = "Retrieve next sample";
                    break;
                case 33:
                    stage = 33;
                    //Get sum
                    z = (weight1*input1) + (weight4*input2);
                    sum1 = z;
                    tip = "We sum the inputs multiplied by their weights, giving us a result of "+(Math.round(sum1 * 100.0) / 100.0);
                    log = "Get sum of inputs (Top neuron)";
                    break;
                case 34:
                    stage = 34;
                    //Get sum
                    z = (weight2*input1) + (weight5*input2);
                    sum2 = z;
                    tip = "We sum the inputs multiplied by their weights, giving us a result of "+(Math.round(sum2 * 100.0) / 100.0);
                    log = "Get sum of inputs (Middle neuron)";
                    break;
                case 35:
                    stage = 35;
                    //Get sum
                    z = (weight3*input1) + (weight6*input2);
                    sum3 = z;
                    tip = "We sum the inputs multiplied by their weights, giving us a result of "+(Math.round(sum3 * 100.0) / 100.0);
                    log = "Get sum of inputs (Bottom neuron)";
                    break;
                case 36:
                    stage = 36;
                    if (Objects.equals(activation, "Sigmoid")) {
                        function1 = Functions.Sigmoid(sum1);
                    } else if (Objects.equals(activation, "ReLu")) {
                        function1 = Functions.ReLu(sum1);
                    }
                    tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum1 * 100.0) / 100.0)+") = "+function1;
                    log = "Apply activation function ("+MainWindow.activation+")";
                    break;
                case 37:
                    stage = 37;
                    if (Objects.equals(activation, "Sigmoid")) {
                        function2 = Functions.Sigmoid(sum2);
                    } else if (Objects.equals(activation, "ReLu")) {
                        function2 = Functions.ReLu(sum2);
                    }
                    tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum2 * 100.0) / 100.0)+") = "+function2;
                    log = "Apply activation function ("+MainWindow.activation+")";
                    break;
                case 38:
                    stage = 38;
                    if (Objects.equals(activation, "Sigmoid")) {
                        function3 = Functions.Sigmoid(sum3);
                    } else if (Objects.equals(activation, "ReLu")) {
                        function3 = Functions.ReLu(sum3);
                    }
                    tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum3 * 100.0) / 100.0)+") = "+function3;
                    log = "Apply activation function ("+MainWindow.activation+")";
                    break;
                case 39:
                    stage = 39;

                    sum4 = Math.round(((function1*weightOut1) + (function2*weightOut2) + (function3*weightOut3)) * 100.0) / 100.0;
                    tip = "We sum our activated values multiplied by our weights to get the network output";
                    log = "Sum outputs";
                    break;
                case 40:
                    stage = 40;
                    if (Objects.equals(activation, "Sigmoid")) {
                        out = Functions.Sigmoid(sum4);
                    } else if (Objects.equals(activation, "ReLu")) {
                        //By default, now output will always be sigmoid
                        out = Functions.Sigmoid(sum4);
                    }
                    tip = "We then apply the activation function. In this case we are using "+MainWindow.activation+". f("+(Math.round(sum4 * 100.0) / 100.0)+") = "+out;
                    log = "Apply activation function ("+MainWindow.activation+")";
                    break;
                case 41:
                    stage = 41;
                    sample = data.get(row);

                    //Get error and add to performance graph
                    float[][] error = {{Float.parseFloat(sample.get(2))},{(float) out}};
                    if (Objects.equals(costMetric, "Mean Squared Error")) {
                        cost = (float) Functions.MeanSquaredError(error);
                    } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                        cost = (float) Functions.MeanAbsoluteError(error);
                    }

                    //Add new error point to be plotted
                    MainWindow.testPoints.add(MainWindow.testPoints.size(),cost);
                    //Now refresh dataset so it can be displayed
                    MainWindow.dataset = MainWindow.createDataset(trainPoints,testPoints);

                    if (out == Float.parseFloat(sample.get(2))) {
                        tip = "Our prediction is correct!";
                    } else {
                        if (Objects.equals(costMetric, "Mean Squared Error")) {
                            tip = "We can see our prediction was Incorrect! The cost with MSE is "+cost;
                        } else if (Objects.equals(costMetric, "Mean Absolute Error")) {
                            tip = "We can see our prediction was Incorrect! The cost with MAE is "+cost;
                        }

                    }
                case 42:

                    System.out.println("sample is: "+currentSample);
                    System.out.println("sample count is: "+sampleCount);

                    //Before trying to retrieve the next sample, let's check if this was the last
                    if (MainWindow.sampleCount-1 == currentSample) {
                        //This was the last sample, so stop the program
                        tip = "Dataset Complete!";
                        MainWindow.dataComplete = true;
                        MainWindow.pause = true;
                        log = "Testing Finished!";

                        //Notify the user the network is finished
                        try {
                            MainWindow.displayTray("Network Finished!","The network has finished processing all data samples");
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }

                    } else {
                        //This was not the last sample, so retrieve the next

                        tip = "We're now ready to test with the next sample";
                        log = "Sample Finished";

                        step = 32;

                        //Append the data table row so that the next sample is retrieved for the next cycle
                        row = row+1;
                        //Also update the sample number within MainWindow
                        MainWindow.currentSample = row;

                        //And increment the data label that is displayed, so it shows the new sample which is being dealt with
                        DataTable.rowOffset = DataTable.rowOffset + 1;
                    }

                    break;

                default:
                    System.out.println("Error! No new instructions for testing found");
            }

        }
    }

}
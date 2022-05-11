package Sandbox;

import Interface.MainWindow;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static Interface.MainWindow.currentSample;

public class PerceptronController {

    //Initialise this for later reference
    private static double z = 0f;
    public static int step = 0;
    public static int stage = 0;

    //This will be used to recall if the network has been initialised before (so it is not done again)
    public static boolean hasBeenInitialised = false;

    //Inputs and Outputs
    public static double input1 = 0;
    public static double input2 = 0;
    public static double input3 = 0;
    public static double output = 0;

    public static double initWeight1 = 0;
    public static double initWeight2 = 0;
    public static double initWeight3 = 0;

    //Weights to display
    public static double weight1 = 0f;
    public static double weight2 = 0f;
    public static double weight3 = 0f;

    //Neuron Sums
    //'z' value of all summed inputs to a neuron
    public static double sum;
    //'a' value for the activation output
    public static double function;

    //RetentionWeights
    //These will be used to remember the previous weight prior to the most recent adjustment
    public static double oldWeight1 = 0f;
    public static double oldWeight2 = 0f;
    public static double oldWeight3 = 0f;

    //Sample row
    public static int row = 0;
    //Data
    public static List<List<String>> data = DataFeed.data(MainWindow.dataSetFile);

    //Output
    public static float out = 0f;

    //Text to display as explanation
    public static String tip = "A perceptron. We have 3 inputs, a single neuron with Heaviside activation, and an output";
    //Text to store as log entry
    public static String log = "Error! No log entry to display";


    public static void reset() {

        try {
            MainWindow.displayTray("Network Reset","The network has been reset to its original state");
        } catch (AWTException e) {
            e.printStackTrace();
        }

        step = 0;
        stage = 0;
        //Inputs and Outputs
        input1 = 0;
        input2 = 0;
        input3 = 0;
        output = 0;

        initWeight1 = 0;
        initWeight2 = 0;
        initWeight3 = 0;

        function = 0f;

        //Weights to display
        weight1 = 0f;
        weight2 = 0f;
        weight3 = 0f;

        sum = 0f;
        out = 0f;

        row = 0;
        hasBeenInitialised = false;

        currentSample = 0;

    }

    public static double getSum(double input1, double input2, double input3, double weight1, double weight2, double weight3) {
        return input1*weight1+
        input2*weight2+
        input3*weight3;
    }

    public static void previousStep() {
        //The user wishes to do the previous step

        if (step == 3) {
            //The user is on step 3 (summation) and clicked back
            //Reset the sum value
            sum = 0;
        } else if (step == 4) {
            //The user is on step 4 (activation) and clicked back
            //Reset the function value
            function = 0;
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
            tip = "A perceptron. We have 3 inputs, a single neuron with Heaviside activation, and an output";
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

        if (!MainWindow.testing) {

            switch (step) {
                case 0:
                    //Default state (not pressed forward yet)
                    stage = 0;
                    weight1 = 0f;
                    weight2 = 0f;
                    weight3 = 0f;
                    out = 0;

                    //Get number of rows of the loaded dataset that are being worked with
                    //This is used for the progress bar
                    MainWindow.sampleCount = data.size();

                    tip = "A perceptron. We have 3 inputs, a single neuron with Heaviside activation, and an output";
                    log = ""; //This is an empty action, so create no log entry
                case 1:
                    stage = 1;
                    //initialise weights
                    System.out.println("Step 1: Initialise weights (" + MainWindow.initialisation + ")");

                    //Reset the input nodes in case the user steps backwards
                    input1 = 0f;
                    input2 = 0f;
                    input3 = 0f;

                    if (!hasBeenInitialised) {
                        if (Objects.equals(MainWindow.initialisation, "Random")) {
                            weight1 = Functions.initialiseRandom();
                            weight2 = Functions.initialiseRandom();
                            weight3 = Functions.initialiseRandom();
                            initWeight1 = weight1;
                            initWeight2 = weight2;
                            initWeight3 = weight3;
                            hasBeenInitialised = true;
                        } else if (Objects.equals(MainWindow.initialisation, "Xavier")) {
                            weight1 = Functions.initialiseXavier(3, 1);
                            weight2 = Functions.initialiseXavier(3, 1);
                            weight3 = Functions.initialiseXavier(3, 1);
                            initWeight1 = weight1;
                            initWeight2 = weight2;
                            initWeight3 = weight3;
                            hasBeenInitialised = true;
                        }
                    } else {
                        //The network has been initialised previously, so simply set weights to their original initialised values
                        weight1 = initWeight1;
                        weight2 = initWeight2;
                        weight3 = initWeight3;
                    }

                    out = 0;
                    if (Objects.equals(MainWindow.initialisation, "Random")) {
                        tip = "Initialise the weights randomly with values between -1 and 1";
                    } else if (Objects.equals(MainWindow.initialisation, "Xavier")) {
                        tip = "Initialise the weights randomly in a uniform distribution (Xavier)";
                    }
                    log = "Initialise Weights (" + MainWindow.initialisation + ")";
                    break;
                case 2:
                    stage = 2;
                    //Get sum
                    System.out.println("Step 2: Get next sample");

                    //Get the next row of the dataset
                    List<String> sample = data.get(row);

                    input1 = Double.parseDouble(sample.get(0));
                    input2 = Double.parseDouble(sample.get(1));
                    input3 = Double.parseDouble(sample.get(2));
                    output = Double.parseDouble(sample.get(3));

                    tip = "We get the next sample from the dataset. Our desired output is " + output;
                    log = "Retrieve next sample";
                    break;
                case 3:
                    stage = 3;
                    //Get sum
                    System.out.println("Step 3: Get sum of inputs");
                    z = getSum(input1, input2, input3, weight1, weight2, weight3);
                    sum = z;
                    tip = "We sum the inputs multiplied by their weights, giving us a result of " + Math.round(z * 100.0) / 100.0;
                    log = "Get sum of inputs";
                    break;
                case 4:
                    stage = 4;
                    System.out.println("Step 4: Run through step function");
                    out = Functions.HeavisideStep(z);
                    function = out;
                    tip = "We then apply the activation function. In this case we are using Heavside Step. f(" + Math.round(z * 100.0) / 100.0 + ") = " + out;
                    log = "Apply activation function";
                    break;
                case 5:
                    stage = 5;
                    System.out.println("Step 5: Check output");
                    sample = data.get(row);

                    //Get error and add to performance graph
                    float error = Float.parseFloat(sample.get(3)) - out;
                    System.out.println("Error was: " + error + ". Adding point.");
                    //Add new error point
                    MainWindow.trainPoints.add(MainWindow.trainPoints.size(), error);
                    //Now refresh dataset so it can be displayed
                    MainWindow.dataset = MainWindow.createDataset(MainWindow.trainPoints,MainWindow.testPoints);

                    if (out == Integer.parseInt(sample.get(3))) {
                        tip = "Our prediction is correct,so no weight adjustments are required.";
                        step = 9;
                    } else {
                        tip = "We can see our prediction was Incorrect! . . Adjustments to the weights are needed.";
                    }
                    log = "Check whether output is correct";

                    break;
                case 6:

                    //Check if the user just stepped back from step 8
                    if (stage == 7) {
                        //If so, revert weight1 to its value prior to adjustment
                        weight1 = oldWeight1;
                    }

                    stage = 6;
                    System.out.println("Step 6: Adjust Weights");

                    tip = "We can adjust our network by applying this learning rule to each weight, where n is the learning rate.";
                    log = "";

                    oldWeight1 = weight1;
                    oldWeight2 = weight2;
                    oldWeight3 = weight3;

                    break;
                case 7:
                    //Check if the user just stepped back from step 8
                    if (stage == 8) {
                        //If so, revert weight1 to its value prior to adjustment
                        weight2 = oldWeight2;
                    }

                    stage = 7;
                    System.out.println("Step 7: Adjust Weight 1");

                    sample = data.get(row);

                    weight1 = Functions.PerceptronLearningRule(MainWindow.learningRate, Double.parseDouble(sample.get(3)), out, input1);

                    tip = "The input was " + input1 + ", and our target was " + sample.get(3) + ". The learning rate is " + MainWindow.learningRate + " so this weight is adjusted to " + weight1;
                    log = "Adjust weight 1";

                    break;
                case 8:
                    //Check if the user just stepped back from step 9
                    if (stage == 9) {
                        //If so, revert weight1 to its value prior to adjustment
                        weight3 = oldWeight3;
                    }

                    stage = 8;
                    System.out.println("Step 8: Adjust Weight 2");

                    sample = data.get(row);

                    weight2 = Functions.PerceptronLearningRule(MainWindow.learningRate, Double.parseDouble(sample.get(3)), out, input2);

                    tip = "The input was " + input2 + ", and our target was " + sample.get(3) + ". The learning rate is " + MainWindow.learningRate + " so this weight is adjusted to " + weight2;
                    log = "Adjust weight 2";
                    break;
                case 9:
                    stage = 9;
                    System.out.println("Step 9: Adjust Weight 3");

                    sample = data.get(row);

                    weight3 = Functions.PerceptronLearningRule(MainWindow.learningRate, Double.parseDouble(sample.get(3)), out, input3);

                    tip = "The input was " + input3 + ", and our target was " + sample.get(3) + ". The learning rate is " + MainWindow.learningRate + " so this weight is adjusted to " + weight3;
                    log = "Adjust weight 3";
                    break;
                case 10:
                    stage = 10;
                    System.out.println("Step 10: Sample Finished");

                    tip = "We're now ready for the next sample. Now repeat from Step 2 with new data!";
                    log = "Sample Finished";

                    step = 1;
                    stage = 1;

                    //Append the data table row so that the next sample is retrieved for the next cycle
                    row = row + 1;
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
                        System.out.println("Switching phase to testing!");
                        //The step will also be reset for the next phase
                        step = 11;
                    }

                    //Also increment the data table that is displayed, so it shows the new sample which is being dealt with
                    DataTable.rowOffset = DataTable.rowOffset + 1;

                    break;

                default:
                    //initialise weights
                    System.out.println("Error! No new steps found?");
            }

        } else {
            //The training phase is complete, now switch to testing

            switch (step) {
                case 11:
                    stage = 11;
                    //Get sum
                    System.out.println("Step 2: Get next sample");

                    //Get the next row of the dataset
                    List<String> sample = data.get(row);

                    input1 = Double.parseDouble(sample.get(0));
                    input2 = Double.parseDouble(sample.get(1));
                    input3 = Double.parseDouble(sample.get(2));
                    output = Double.parseDouble(sample.get(3));

                    tip = "We get the next sample from the dataset. Our desired output is " + output;
                    log = "Retrieve next sample";
                    break;
                case 12:
                    stage = 12;
                    //Get sum
                    z = getSum(input1, input2, input3, weight1, weight2, weight3);
                    sum = z;
                    tip = "We sum the inputs multiplied by their weights, giving us a result of " + Math.round(z * 100.0) / 100.0;
                    log = "Get sum of inputs";
                    break;
                case 13:
                    stage = 13;
                    out = Functions.HeavisideStep(z);
                    function = out;
                    tip = "We then apply the activation function. In this case we are using Heavside Step. f(" + Math.round(z * 100.0) / 100.0 + ") = " + out;
                    log = "Apply activation function";
                    break;
                case 14:
                    stage = 14;
                    sample = data.get(row);
                    //Get error and add to performance graph
                    float error = Float.parseFloat(sample.get(3)) - out;
                    //Add new error point
                    MainWindow.testPoints.add(MainWindow.testPoints.size(), error);
                    //Now refresh dataset so it can be displayed
                    MainWindow.dataset = MainWindow.createDataset(MainWindow.trainPoints,MainWindow.testPoints);

                    if (out == Integer.parseInt(sample.get(3))) {
                        tip = "Our prediction is correct!";
                    } else {
                        tip = "We can see our prediction was Incorrect!";
                    }
                    log = "Check whether output is correct";

                    break;
                case 15:
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

                        step = 11;

                        //Append the data table row so that the next sample is retrieved for the next cycle
                        row = row + 1;
                        //Also update the sample number within MainWindow
                        MainWindow.currentSample = row;

                        //And increment the data label that is displayed so it shows the new sample which is being dealt with
                        DataTable.rowOffset = DataTable.rowOffset + 1;
                    }

                    break;

            }


        }
    }

}

package Interface;

import Sandbox.DataFeed;
import Sandbox.DataTable;
import Sandbox.PerceptronController;
import Sandbox.SimpleNetworkController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MainWindow extends JFrame {

    private JPanel MainPanel;
    public JPanel Viewport;
    private JLabel sampleSpeed;
    private JLabel Mode;
    private JButton back;
    private JButton forward;
    private JButton stopGoButton;
    private JButton fasterButton;
    private JButton slowerButton;
    private JCheckBox verboseTick;
    public JTextArea ActionLog;
    private JCheckBox showDataTick;
    private JCheckBox tipsTick;
    private JPanel GraphPanel;
    private JComboBox<String> initialisationCombo;
    private JComboBox<String> activationCombo;
    private JComboBox<String> costCombo;
    private JCheckBox colourByWeightTick;
    private JSlider learningRateSlider;
    private JSlider trainTestSlider;
    private JProgressBar TrainingProgressBar;
    private JLabel sampleCountLabel;
    private JLabel sampleReservationLabel;
    private JProgressBar TestingProgressBar;
    private JButton resetButton;
    public JPanel plot;
    //These panels aren't referenced but still need to be defined here. This is not a typo!
    public JPanel graph;
    private JPanel HeaderPanel;
    private JLabel EpochLabel;
    private JPanel AutoControls;
    private JPanel ControlPanel;
    private JPanel Buttons;
    private JPanel ManualControls;
    private JPanel Setting2;
    private JPanel Setting3;
    private JPanel Settings;
    private JPanel LeftControl;
    private JPanel RightControl;

    public static XYSeries series1 = new XYSeries("Train");
    public static XYSeries series2 = new XYSeries("Test");

    //Default settings
    public static boolean pause = true;
    public static boolean verbose = false;
    public static boolean tipsEnabled = false;
    public static boolean colourByWeight = false;
    public static boolean dataTableEnabled = false;
    public static double learningRate = 0.5;
    public static int zoom;
    //Default options
    public static String costMetric = "Mean Squared Error";
    public static String initialisation = "Random";
    public static String activation = "Sigmoid";
    //This should be set to the dataset the user intends to use (e.g. 'test.csv')
    public static String dataSetFile = "test2.csv";

    public static int sampleCount; //The number of total samples the data csv file has. This will be set when a new dataset is loaded
    public static int currentSample; //Keep track of the current sample that is being worked on
    public static int samplesForTesting; //Store the number of samples that are reserved for testing
    public static int samplesForTraining; //Store the number of samples that are to be used for training

    //Default mode will be the perceptron
    public static int mode = 1;

    //The initial set of instructions for the network will be to train
    //When this is set to false, the sandbox will look to only test with the remaining samples in the dataset
    public static boolean testing = false;
    //Meanwhile, this will be used to remember if the dataset is finished with
    //If it is set to true, then the controller classes know to not allow the user to do another step forward (since there is no more data to work with)
    public static boolean dataComplete = false;

    //Automatic Stepping (in ms)
    public int speed = 50; //Default speed was 4000
    public int minSpeed = 10000; //Min speed is 1 steps / 10 seconds
    public int maxSpeed = 250; //Max speed is 1 step / second

    public Timer timer = new Timer(false);

    public int interval = 0;

    //Performance Log Entries
    public static ArrayList trainPoints = new ArrayList() {{
        add(0f);
    }};
    public static ArrayList testPoints = new ArrayList() {{
        add(0f);
    }};
    public static XYDataset dataset = createDataset(trainPoints,testPoints);

    public static BufferedImage icon;




    public MainWindow() {

        //Load icon for window
        try {
            icon = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/logoIcon.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        List<List<String>> data = DataFeed.data(dataSetFile);
        sampleCount = data.size();
        sampleCountLabel.setText("Sample "+currentSample+" of "+(sampleCount-1));

        //Set slider parameters
        learningRateSlider.setForeground(Color.decode("#B75C5C"));
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("0"));
        labels.put(100, new JLabel("1"));
        learningRateSlider.setLabelTable(labels);
        learningRateSlider.setMajorTickSpacing(25);
        learningRateSlider.setMinorTickSpacing(5);
        learningRateSlider.setPaintTicks(true);
        learningRateSlider.setPaintLabels(true);
        learningRateSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        //Set slider parameters
        trainTestSlider.setForeground(Color.decode("#B75C5C"));
        Hashtable<Integer, JLabel> splitLabels = new Hashtable<>();
        splitLabels.put(0, new JLabel("0"));
        splitLabels.put(20, new JLabel("20"));
        splitLabels.put(40, new JLabel("40"));
        splitLabels.put(60, new JLabel("60"));
        splitLabels.put(80, new JLabel("80"));
        splitLabels.put(100, new JLabel("100"));
        trainTestSlider.setLabelTable(splitLabels);
        trainTestSlider.setMajorTickSpacing(20);
        trainTestSlider.setMinorTickSpacing(5);
        trainTestSlider.setPaintTicks(true);
        trainTestSlider.setPaintLabels(true);
        trainTestSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        //Adjust the 'learningRate' variable based on slider value
        learningRateSlider.addChangeListener(e -> learningRate = (double) learningRateSlider.getValue() / 100);

        //Add combo items
        initialisationCombo.addItem("Random");
        initialisationCombo.addItem("Xavier");

        costCombo.addItem("Mean Squared Error");
        costCombo.addItem("Mean Absolute Error");
        //If the user is using the perceptron template, only show Heaviside
        if (mode == 1) {
            activationCombo.addItem("Heaviside");
            activationCombo.setEnabled(false);
            activation = "Heaviside";
        } else {
            //The user is not running a perceptron model, and so add regular activations
            activationCombo.addItem("Sigmoid");
            activationCombo.addItem("ReLu");
            //Sigmoid will be the default activation for networks which aren't perceptrons
            activation = "Sigmoid";
        }

        //Combo Listeners
        initialisationCombo.addActionListener(e -> {
            //Set initialisation technique according to what the user chose
            initialisation = Objects.requireNonNull(initialisationCombo.getSelectedItem()).toString();
            System.out.println("Initialisation was changed to "+initialisationCombo.getSelectedItem().toString());
        });

        activationCombo.addActionListener(e -> {
            //Set activations according to what the user chose
            activation = Objects.requireNonNull(activationCombo.getSelectedItem()).toString();
            System.out.println("Activation was changed to "+activationCombo.getSelectedItem().toString());
        });

        costCombo.addActionListener(e -> {
            //Set cost according to what the user chose
            costMetric = Objects.requireNonNull(costCombo.getSelectedItem()).toString();
            System.out.println("Cost metric was changed to "+ Objects.requireNonNull(activationCombo.getSelectedItem()));
        });

        //Set default labels
        sampleSpeed.setText("Delay (ms): "+(speed));

        //Load network template based on mode
        if (mode == 1) {
            plot = new Perceptron();
            dataSetFile = "test.csv";
        } else if (mode == 2) {
            plot = new SimpleNetwork();
            dataSetFile = "test2.csv";
        }  //No mode found

        plot.setOpaque(true);
        plot.setBackground(Color.WHITE);
        plot.setPreferredSize(new Dimension(200, 200));
        Viewport.setBackground(Color.CYAN);
        Viewport.add(plot);
        Viewport.revalidate();



        //Add mouse listener for zoom
        Viewport.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                //Check the zoom does not go above max level
                if (zoom < 30) {
                    zoom = zoom + e.getScrollAmount();
                }

            } else {
                //Check the zoom does not go below min level
                if (zoom > -6) {
                    zoom = zoom - e.getScrollAmount();
                }

            }
            System.out.println("Zoom level changed to: "+zoom);
            Viewport.repaint();
        });

        //Performance plot
        JFreeChart chart = ChartFactory.createXYLineChart("", "Episode", "Error", dataset);
        //might be this causing problems. remove transparency and add same colour as form
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        //chart.removeLegend();
        chart.getLegend().setBackgroundPaint(new Color(0, 0, 0, 0));
        chart.getLegend().setBorder(0,0,0,0);
        ChartPanel chartPanel = new ChartPanel(chart);
        GraphPanel.setLayout(new java.awt.BorderLayout());
        GraphPanel.add(chartPanel,BorderLayout.CENTER);
        GraphPanel.validate();

        verboseTick.addChangeListener(e -> {
            verbose = verboseTick.isSelected();
            Viewport.repaint();
        });



        forward.addActionListener(e -> {
            System.out.println("Forward pressed");
            //Disable initialisation controls (once the user has pressed forwards, the network is always started/initialised)
            disableInitialisationSettings();
            if (mode == 1) {
                //Perceptron
                PerceptronController.nextStep();
                updateLog(PerceptronController.log);
                updateProgressbar();
            } else if (mode == 2) {
                //Simple Neural Network
                SimpleNetworkController.nextStep();
                updateLog(SimpleNetworkController.log);
                updateProgressbar();
            }
            //Update viewport with controller values
            Viewport.repaint();

        });

        back.addActionListener(e -> {
            System.out.println("Backwards pressed");
            if (mode == 1) {
                //Perceptron
                PerceptronController.previousStep();
            } else if (mode == 2) {
                //Simple Neural Network
                SimpleNetworkController.previousStep();
            }
            //Update viewport with controller values
            Viewport.repaint();
        });

        tipsTick.addActionListener(e -> {
            tipsEnabled = tipsTick.isSelected();
            Viewport.repaint();
        });

        colourByWeightTick.addActionListener(e -> {
            colourByWeight = !MainWindow.colourByWeight;
            Viewport.repaint();
        });

        showDataTick.addActionListener(e -> {
            dataTableEnabled = showDataTick.isSelected();
            Viewport.repaint();
        });

        stopGoButton.addActionListener(e -> {

            disableInitialisationSettings();

            if (pause) {
                //The timer is paused, so turn it on
                pause = false;
                Mode.setText("Mode: Auto");
                timer.purge();
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //Run every interval
                        if (!pause) {
                            interval = interval+1;
                            if (mode == 1) {
                                PerceptronController.nextStep();
                                updateLog(PerceptronController.log);
                                updateProgressbar();
                            } else if (mode == 2) {
                                SimpleNetworkController.nextStep();
                                updateLog(SimpleNetworkController.log);
                                updateProgressbar();
                            }
                            //Update viewport with controller values
                            Viewport.repaint();
                        }
                    }
                }, 0, speed);
            } else {
                //The timer is on, so pause it
                pause = true;
                timer.purge();
                timer.cancel();
                Mode.setText("Mode: Manual");
            }
        });

        fasterButton.addActionListener(e -> {
            if (speed > maxSpeed) {
                speed = speed - 250;
            }
            sampleSpeed.setText("Delay (ms): "+(speed));

            timer.purge();
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //Run every interval
                    if (!pause) {
                        interval = interval+1;
                        if (mode == 1) {
                            PerceptronController.nextStep();
                            updateLog(PerceptronController.log);
                            updateProgressbar();
                        } else if (mode == 2) {
                            SimpleNetworkController.nextStep();
                            updateLog(SimpleNetworkController.log);
                            updateProgressbar();
                        }
                        //Update viewport with controller values
                        Viewport.repaint();
                    }
                }
            }, 0, speed);
        });

        slowerButton.addActionListener(e -> {
            if (speed < minSpeed) {
                speed = speed+250;
            }
            sampleSpeed.setText("Delay (ms): "+(speed));

            timer.purge();
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //Run every interval
                    if (!pause) {
                        interval = interval+1;
                        if (mode == 1) {
                            PerceptronController.nextStep();
                            updateLog(PerceptronController.log);
                            updateProgressbar();
                        } else if (mode == 2) {
                            SimpleNetworkController.nextStep();
                            updateLog(SimpleNetworkController.log);
                            updateProgressbar();
                        }
                        //Update viewport with controller values
                        Viewport.repaint();
                    }
                }
            }, 0, speed);
        });


        resetButton.addActionListener(e -> {
            //Pause automatic mode if it is on
            pause = true;
            Mode.setText("Mode: Manual");
            //Open a dialogue box to confirm the user's choice
            Object[] options = {"Cancel", "Reset"};
            UIManager.put("OptionPane.background", Color.decode("#ffebe6"));
            UIManager.getLookAndFeelDefaults().put("Panel.background", Color.decode("#ffebe6"));
            UIManager.put("Button.background", Color.decode("#e8745f"));

            int resetConfirmation = JOptionPane.showOptionDialog(null,
                    "Are you sure you want to reset the network to its original state?",
                    "Reset Network",
                    JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (resetConfirmation == 1) {
                //The user confirmed their choice
                System.out.println("Resetting network . .");

                if (mode == 1) {
                    PerceptronController.reset();
                } else if (mode == 2) {
                    SimpleNetworkController.reset();
                } else {
                    System.out.println("Error! No mode found");
                }
                currentSample = 0;
                dataComplete = false;
                testing = false;
                initialisationCombo.setEnabled(true);
                initialisationCombo.repaint();
                trainTestSlider.setEnabled(true);
                trainTestSlider.repaint();
                samplesForTesting = 0;
                samplesForTraining = 0;
                TrainingProgressBar.setValue(0);
                TestingProgressBar.setValue(0);
                ActionLog.setText("");
                sampleReservationLabel.setText("");
                //Reset the data table row offset, so it goes back to showing the first samples
                DataTable.rowOffset = 0;
                sampleCountLabel.setText("Sample "+currentSample+" of "+(sampleCount-1));
                //Repaint the viewport with reset visuals
                plot.repaint();
                //Remove previous error reports
                series1.clear();
                series2.clear();
                //Reset the points too
                testPoints = new ArrayList<Float>() {{add(0f);}};
                trainPoints = new ArrayList<Float>() {{add(0f);}};
                //Repaint the graph now the data has been removed
                chart.fireChartChanged();
            }
        });
    }

    public static void displayTray(String caption, String message) throws AWTException {

        if (SystemTray.isSupported()) {
            System.out.println("Notification opened");
            SystemTray notif = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon notifIcon = new TrayIcon(image);
            notifIcon.setImageAutoSize(true);
            notif.add(notifIcon);
            notifIcon.displayMessage(caption, message, TrayIcon.MessageType.NONE);
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(MainWindow::createAndShowGUI); }

    public void disableInitialisationSettings () {
        //This should be called when the network has been initialised.
        //The 'initialisation' method should be disabled, since the original weights should always be remembered
        //The data hold-out for testing should also stay as its originally set value after the network is running
        initialisationCombo.setEnabled(false);
        initialisationCombo.repaint();
        trainTestSlider.setEnabled(false);
        trainTestSlider.repaint();

        //As the network is initialised, we also wish to set the number of samples reserved for testing according to what the user set the hold-out slider as
        //Load the data to know how much there is to work with
        List<List<String>> data = DataFeed.data(dataSetFile);
        sampleCount = data.size();
        sampleCountLabel.setText("Sample "+currentSample+" of "+(sampleCount-1));
        //And then determine how many rows to use for each phase based on user input
        //It may seem odd to cast to double and then back to int, but this is necessary with how Java handles division differently for integers
        samplesForTesting = (int) (((double)trainTestSlider.getValue()/100) * sampleCount);
        samplesForTraining = sampleCount-samplesForTesting;
        //Set the label on the interface to show how many samples will be used for testing
        sampleReservationLabel.setText("Samples reserved for testing: "+samplesForTesting);

    }

    public void updateLog(String text) {
        //If the update passed is empty, it should not be added to the log
        //This function is called every step, so this is necessary
        if (!Objects.equals(text, "")) {
            //If there are more than 13 lines/entries, remove the first (the furthest back) before appending a new line
            if (ActionLog.getText().split("\r\n|\r|\n").length > 13) { //was 13 then the lines was 10 on the element
                //Remove last line of text
                int end = 0;
                try {
                    end = ActionLog.getLineEndOffset(0);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                ActionLog.replaceRange("", 0, end);
            }
            //Add the new entry to the action log
            ActionLog.setText(ActionLog.getText()+"\n"+text);
        }

    }

    public void updateProgressbar() {
        //The casting here may seem out of place, but it is necessary for how Java handles integer division differently
        if (!testing) {
            //The phase is training, so we wish to update the training progress bar
            //Set the bar percentage
            double a = (30+(1300/((double) samplesForTraining-1))*(currentSample));
            TrainingProgressBar.setValue((int) Math.round(a));
        } else {
            //The phase is testing, so we wish to update the testing progress bar
            //Set the bar percentage
            double f = (30+(1300/((double) samplesForTesting-1))*((currentSample)-samplesForTraining));
            TestingProgressBar.setValue((int) Math.round(f));
        }

        //Now set label
        sampleCountLabel.setText("Sample "+currentSample+" of "+(sampleCount-1));

    }

    public static XYDataset createDataset(List<Float> trainPoints,List<Float> testPoints) {
        //Define dataset
        XYSeriesCollection dataset = new XYSeriesCollection();

        //Add each point to the series to be plotted
        for (int i = 1; i < trainPoints.size(); i++) {
            float error = trainPoints.get(i);
            series1.add(i,error);
        }
        //Add each point to the series to be plotted
        for (int i = 1; i < testPoints.size(); i++) {
            float error = testPoints.get(i);
            series2.add(i,error);
        }
        //Add the series to the dataset
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    public static void createAndShowGUI() {

        //Create the main window for the application
        System.out.println("Main window created");
        MainWindow frame = new MainWindow();
        frame.setContentPane(new MainWindow().MainPanel);
        frame.setVisible(true);
        frame.setTitle("Garnet");
        frame.setIconImage(icon);
        frame.setSize(1200,840);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    System.out.println("Quit pressed");
                    frame.dispose();
                    TemplateSelector.createAndShowGUI();
                }
            }
        });
        frame.requestFocus();

    }
}

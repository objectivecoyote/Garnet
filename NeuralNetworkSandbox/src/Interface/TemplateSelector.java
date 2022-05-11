package Interface;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class TemplateSelector extends JFrame {
    private JButton StartButton;
    private JComboBox<String> NetworkSelector;
    private JPanel MainPanel;
    public static BufferedImage icon;
    public static TemplateSelector frame = new TemplateSelector();
    //These two lines are needed
    private JPanel logoPanel;
    private JLabel logoLabel;

    public TemplateSelector() {
        //Load icon for window
        try {
            icon = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/logoIcon.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //Add options to the dropdown
        NetworkSelector.addItem("Perceptron");
        NetworkSelector.addItem("Simple Network");

        StartButton.addActionListener(e -> {
            if (NetworkSelector.getSelectedItem() == "Perceptron") {

                MainWindow.mode = 1;
                MainWindow.createAndShowGUI();
                frame.dispose();

            } else if (NetworkSelector.getSelectedItem() == "Simple Network") {

                MainWindow.mode = 2;
                MainWindow.createAndShowGUI();
                frame.dispose();

            }
        });
    }

    public static void createAndShowGUI() {

        System.out.println("Menu Selector opened");
        frame.setContentPane(new TemplateSelector().MainPanel);
        frame.setTitle("Garnet");
        frame.setIconImage(icon);
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

    }

    public static void main(String[] args) { SwingUtilities.invokeLater(TemplateSelector::createAndShowGUI); }
}

package Interface;
import Sandbox.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static Interface.MainWindow.*;
import static Sandbox.PerceptronController.stage;

public class Perceptron extends JPanel {

    //Buffer the image for summation for when needed
    private BufferedImage image;
    private BufferedImage sum;
    private BufferedImage function;
    private BufferedImage heaviside;
    private BufferedImage learningRule;

    public Perceptron() {
        //Load resources
        try {
            image = ImageIO.read(Objects.requireNonNull(Perceptron.class.getResourceAsStream("/Resources/sumFunction.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error! Resource image not found: "+"/Resources/sumFunction.png");
        }

        try {
            sum = ImageIO.read(Objects.requireNonNull(Perceptron.class.getResourceAsStream("/Resources/sum.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error! Resource image not found: "+"/Resources/sum.png");
        }

        try {
            function = ImageIO.read(Objects.requireNonNull(Perceptron.class.getResourceAsStream("/Resources/function.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error! Resource image not found: "+"/Resources/function.png");
        }

        try {
            heaviside = ImageIO.read(Objects.requireNonNull(Perceptron.class.getResourceAsStream("/Resources/heavisideActivation.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error! Resource image not found: "+"/Resources/heavisideActivation.png");
        }

        try {
            learningRule = ImageIO.read(Objects.requireNonNull(Perceptron.class.getResourceAsStream("/Resources/PerceptronLearningRule.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error! Resource image not found: "+"/Resources/PerceptronLearningRule.png");
        }


    }

    public int getMidX (int x1, int x2) {
        return (x1+x2)/2;
    }
    public int getMidY (int y1, int y2) {
        return (y1+y2)/2;
    }


    public void paintComponent(Graphics g) {

        Dimension dimension = getSize();

        super.paintComponent(g);

        int radius2 = zoom + ((dimension.width / 23)+(dimension.height/23))/2;
        int diameter2 = radius2 * 2;

        int radius = zoom + ((dimension.width / 20)+(dimension.height/20))/2;

        int y = dimension.height/2;

        int diameter = radius * 2;

        int xpos1=(dimension.width/2)-radius;
        int xpos2=(dimension.width/2)-radius2*8;
        int xpos3=(dimension.width/2)+radius2*6;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        //Input
        g.setColor(Color.decode("#babde8"));
        g.fillOval(xpos2, y - radius2 + diameter2+55, diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos2, y - radius2 + diameter2+55, diameter2, diameter2);

        g.setColor(Color.decode("#babde8"));
        g2.fillOval(xpos2, y - radius2, diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos2, y - radius2, diameter2, diameter2);

        g.setColor(Color.decode("#babde8"));
        g2.fillOval(xpos2, y - radius2 - diameter2 - 55, diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos2, y - radius2 - diameter2 - 55, diameter2, diameter2);

        //Edge from input1 to Neuron
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(PerceptronController.weight1));}
        g.drawLine(xpos2+radius2*2, y + diameter2+55, xpos1, y);
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(PerceptronController.weight2));}
        g.drawLine(xpos2+radius2*2, y, xpos1, y);
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(PerceptronController.weight3));}
        g.drawLine(xpos2+radius2*2, y - diameter2-55, xpos1, y);
        //Edge from neuron to output
        g.setColor(Color.black);
        g.drawLine(xpos1, y, xpos3, y);

        //Neuron
        g.setColor(Color.decode("#e3978d"));
        g.fillOval(xpos1, y - radius, diameter, diameter);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos1, y - radius, diameter, diameter);

        //Output
        g.setColor(Color.decode("#c7e8ba"));
        g.fillOval(xpos3, y - radius2, diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos3, y - radius2, diameter2, diameter2);

        //Update Weights
        Font font = g2.getFont();
        FontRenderContext context = g2.getFontRenderContext();

        //w1
        double weight1 = PerceptronController.weight1;
        g2.setFont(font);
        int textWidth = (int) font.getStringBounds(Double.toString(weight1), context).getWidth();
        LineMetrics ln = font.getLineMetrics(Double.toString(weight1), context);
        int textHeight = (int) (ln.getAscent() + ln.getDescent());
        //if weight 1 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 7) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect(getMidX(xpos2+radius2*2,xpos1)-(textWidth/2)-2, (int) (getMidY(y + diameter2+55,y)-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        g2.drawString(Double.toString(weight1), (getMidX(xpos2+radius2*2,xpos1)-(textWidth/2)), (int) (getMidY(y + diameter2+55,y)+(textHeight/2) - ln.getDescent()));

        //w3
        double weight3 = PerceptronController.weight3;
        textWidth = (int) font.getStringBounds(Double.toString(weight3), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight3), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        if (stage == 9) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect(getMidX(xpos2+radius2*2,xpos1)-(textWidth/2)-2, (int) (getMidY(y - diameter2-55,y)-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        g2.drawString(Double.toString(weight3), (getMidX(xpos2+radius2*2,xpos1)-(textWidth/2)), (int) (getMidY(y - diameter2-55,y)+(textHeight/2) - ln.getDescent()));

        //w2
        double weight2 = PerceptronController.weight2;
        textWidth = (int) font.getStringBounds(Double.toString(weight2), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight2), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        if (stage == 8) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect(getMidX(xpos2+radius2*2,xpos1)-(textWidth/2)-2, (int) (getMidY(y,y)-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        g2.drawString(Double.toString(weight2), (getMidX(xpos2+radius2*2,xpos1)-(textWidth/2)), (int) (getMidY(y,y)+(textHeight/2) - ln.getDescent()));

        //in1
        double input1 = PerceptronController.input1;
        textWidth = (int) font.getStringBounds(Double.toString(input1), context).getWidth();
        ln = font.getLineMetrics(Double.toString(input1), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.drawString(Double.toString(input1), xpos2 + diameter2/2 -(textWidth/2), y - (radius2 * 2) + (diameter2 * 2) + 55 + (textHeight / 2) - ln.getDescent());

        //in2
        double input2 = PerceptronController.input2;
        textWidth = (int) font.getStringBounds(Double.toString(input2), context).getWidth();
        ln = font.getLineMetrics(Double.toString(input2), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.setColor(Color.black);
        g2.drawString(Double.toString(input2), xpos2 + diameter2/2 -(textWidth/2), (int) (y+(textHeight/2) - ln.getDescent()));

        //in3
        double input3 = PerceptronController.input3;
        textWidth = (int) font.getStringBounds(Double.toString(input3), context).getWidth();
        ln = font.getLineMetrics(Double.toString(input3), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.setColor(Color.black);
        g2.drawString(Double.toString(input3), xpos2 + diameter2/2 -(textWidth/2), (y + (radius2*2) - (diameter2*2)-55) +(textHeight/2) - ln.getDescent());

        //out
        float output = PerceptronController.out;
        textWidth = (int) font.getStringBounds(Float.toString(output), context).getWidth();
        ln = font.getLineMetrics(Float.toString(output), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.setColor(Color.black);
        g2.drawString(Float.toString(output), xpos3 + radius2-(textWidth/2), y+(textHeight/2) - ln.getDescent());

        //ADDITIONAL VISUALS

        //Add verbose details
        if (MainWindow.verbose) {

            Font font2 = new Font("Verdana", Font.PLAIN, ((dimension.width / 20)+(dimension.height/20))/3);
            g2.setFont(font2);

            //Summation Symbol
            //Adjust the scale
            int pixX = (int)Math.round((sum.getWidth() * (radius*0.0017)));
            int pixY = (int)Math.round((sum.getHeight() * (radius*0.0017)));
            //Get middle position
            int locX = xpos1+radius/2-(pixX/2);
            int locY = y-(pixY/2);

            //Draw sum symbol
            g.drawImage(sum, locX, locY, pixX, pixY, null);

            //Function symbol
            g.drawImage(function, locX+radius, locY, pixX, pixY, null);

            g2.setFont(font);

            //Sum value
            float sum = (float) PerceptronController.sum;
            textWidth = (int) font.getStringBounds(Float.toString(sum), context).getWidth();
            ln = font.getLineMetrics(Float.toString(sum), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(sum), xpos1-(radius/2)+radius-(textWidth/2), y+(textHeight/2) - ln.getDescent() + (radius/2) + 2);

            //Function value
            float function = (float) PerceptronController.function;
            textWidth = (int) font.getStringBounds(Float.toString(function), context).getWidth();
            ln = font.getLineMetrics(Float.toString(function), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(function), xpos1+(radius/2)+radius-(textWidth/2), y+(textHeight/2) - ln.getDescent() + (radius/2) + 2);

            //Separator Line
            g.drawLine(xpos1+radius, y - radius, xpos1+radius, y+radius);

        }

        if (tipsEnabled) {
            //Tips for verbose mode
            Font font3 = new Font("TimesRoman", Font.PLAIN, ((dimension.width / 30)+(dimension.height/30))/3);
            g2.setFont(font3);
            context = g2.getFontRenderContext();
            String tip = PerceptronController.tip;
            textWidth = (int) font3.getStringBounds(tip, context).getWidth();
            ln = font3.getLineMetrics(tip, context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(tip, dimension.width/2-(textWidth/2), (dimension.height)-(textHeight)- ln.getDescent());
        }

        // Summation
        if (stage == 3 | stage == 12) {
            //Adjust the scale
            int pixX = (int)Math.round((image.getWidth() * (radius*0.004)));
            int pixY = (int)Math.round((image.getHeight() * (radius*0.004)));
            //Get middle position
            int locX = xpos1-(pixX/4);
            int locY = y-(diameter*2)-(pixY/4);

            g.drawImage(image, locX, locY, pixX, pixY, null);
        }

        if (stage == 4 | stage == 13) {
            //Adjust the scale
            int pixX = (int)Math.round((heaviside.getWidth() * (radius*0.004)));
            int pixY = (int)Math.round((heaviside.getHeight() * (radius*0.004)));
            //Get middle position
            int locX = xpos1-(pixX/4);
            int locY = y-(diameter*2)-(pixY/4);

            g.drawImage(heaviside, locX, locY, pixX, pixY, null);
        }

        if (stage == 6 || stage == 7 || stage == 8 || stage == 9) {
            //Adjust the scale
            int pixX = (int)Math.round((learningRule.getWidth() * (radius*0.004)));
            int pixY = (int)Math.round((learningRule.getHeight() * (radius*0.004)));
            //Get middle position
            int locX = xpos1-(pixX/4);
            int locY = y-(diameter*2)-(pixY/4);

            g.drawImage(learningRule, locX, locY, pixX, pixY, null);
        }

        if (dataTableEnabled){
            //Draw table
            DataTable table = new DataTable();

            int xsize = Math.round(dimension.width/2);
            //Center should be mid + x width
            int xmid = (dimension.width/2)-(xsize/2);

            int ysize = (int)Math.round((dimension.height/2)*0.18);

            table.paint(g,xmid,ysize, xsize, ysize);
        }

    }

}

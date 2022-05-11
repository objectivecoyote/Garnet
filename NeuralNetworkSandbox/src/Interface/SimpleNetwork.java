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
import static Sandbox.SimpleNetworkController.stage;

public class SimpleNetwork extends JPanel {

    private BufferedImage image;
    private BufferedImage sum;
    private BufferedImage function;
    private BufferedImage chainSecond;
    private BufferedImage chainFirst;
    private BufferedImage sigmoid;
    private BufferedImage relu;
    private BufferedImage weightAdjustment;

    public double[] coordinate (float startX, float startY, float endX, float endY, float t) {

        //Compute point along the line based on t
        float x = startX + (t/100) * (endX - startX);
        float y = startY + (t/100) * (endY - startY);

        //Create double array with length 2
        double[] coordinate = new double[2];
        //Add points
        coordinate[0] = (int) x;
        coordinate[1] = (int) y;

        //Return point
        return coordinate;
    }

    public SimpleNetwork() {
        //Load resources
        try {
            image = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/sumFunction.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            sum = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/sum.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            function = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/function.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            chainFirst = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/chainRule.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            chainSecond = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/chainRuleSecondLayer.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            sigmoid = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/sigmoid.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            relu = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/ReLu.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            weightAdjustment = ImageIO.read(Objects.requireNonNull(TemplateSelector.class.getResourceAsStream("/Resources/weightAdjustmentBackprop.png")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

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
        g.fillOval(xpos2, (int) (y*1.05 - radius2 + diameter2), diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos2, (int) (y*1.05 - radius2 + diameter2), diameter2, diameter2);

        g.setColor(Color.decode("#babde8"));
        g2.fillOval(xpos2, (int) (y*0.95 - radius2 - diameter2), diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos2, (int) (y*0.95 - radius2 - diameter2), diameter2, diameter2);

        //Hidden
        g.setColor(Color.decode("#e3978d"));
        g.fillOval(xpos1, (int) (y*1.1 - radius + diameter+80), diameter, diameter);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos1, (int) (y*1.1 - radius + diameter+80), diameter, diameter);

        g.setColor(Color.decode("#e3978d"));
        g2.fillOval(xpos1, y - radius, diameter, diameter);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos1, y - radius, diameter, diameter);

        g.setColor(Color.decode("#e3978d"));
        g2.fillOval(xpos1, (int) (y*0.9 - radius - diameter - 80), diameter, diameter);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos1, (int) (y*0.9 - radius - diameter - 80), diameter, diameter);

        // Edges //

        //Update Weights
        //This should go just above where the weights start
        Font font = g2.getFont();
        FontRenderContext context = g2.getFontRenderContext();

        //Inputs to Neurons

        //Edge from top input to middle neuron
        g2.setStroke(new BasicStroke(2));
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weight2));}
        g.drawLine(xpos2+diameter2, (int) (y*0.95 - diameter2), xpos1, y);
        //Edge from bottom input to middle neuron
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weight5));}
        g.drawLine(xpos2+diameter2, (int) (y*1.05 + diameter2), xpos1, y);

        //Edge from bottom input to top neuron
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weight4));}
        g.drawLine(xpos2+diameter2, (int) (y*1.05 + diameter2), xpos1, (int) (y*0.9 - diameter - 80));
        //Edge from top input to top neuron
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weight1));}
        g.drawLine(xpos2+diameter2, (int) (y*0.95 - diameter2), xpos1, (int) (y*0.9 - diameter - 80));

        //Edge from bottom input to bottom neuron
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weight6));}
        g.drawLine(xpos2+diameter2, (int) (y*1.05 + diameter2), xpos1, (int) (y*1.1 + diameter+80));
        //Edge from top input to bottom neuron
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weight3));}
        g.drawLine(xpos2+diameter2, (int) (y*0.95 - diameter2), xpos1, (int) (y*1.1 + diameter+80));

        //Neurons to Outputs --

        //Bottom neuron to output
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weightOut3));}
        g.drawLine(xpos1+diameter, (int) (y*1.1 + diameter+80), xpos3, y);
        //Middle neuron to output
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weightOut2));}
        g.drawLine(xpos1+diameter, y, xpos3, y);
        //Top neuron to output
        if (MainWindow.colourByWeight) {g.setColor(Functions.edgeColour(SimpleNetworkController.weightOut1));}
        g.drawLine(xpos1+diameter, (int) (y*0.9 - diameter - 80), xpos3, y);

        //Output
        g.setColor(Color.decode("#c7e8ba"));
        g.fillOval(xpos3, y - radius2, diameter2, diameter2);
        g2.setColor(Color.BLACK);
        g2.drawOval(xpos3, y - radius2, diameter2, diameter2);

        //w1 - top input to top neuron
        double weight1 = SimpleNetworkController.weight1;
        g2.setFont(font);
        int textWidth = (int) font.getStringBounds(Double.toString(weight1), context).getWidth();
        LineMetrics ln = font.getLineMetrics(Double.toString(weight1), context);
        int textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        double[] weightLoc = coordinate(xpos2+diameter2,(int) (y*0.95 - diameter2),xpos1,(int) (y*0.9 - diameter - 80), 70);
        //If weight 1 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 12 | stage == 13) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weight1), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //w2 - top input to middle neuron
        double weight2 = SimpleNetworkController.weight2;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weight2), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight2), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos2+diameter2,(int) (y*0.95 - diameter2),xpos1,y, 70);
        //If weight 2 is being adjusted, make background yellow to hightlight. Otherwise, keep as white
        if (stage == 14 | stage == 15) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weight2), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //w3 - top input to bottom neuron
        double weight3 = SimpleNetworkController.weight3;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weight3), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight3), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos2+diameter2,(int) (y*0.95 - diameter2),xpos1,(int) (y*1.1 + diameter+80), 70);
        //If weight 3 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 16 | stage == 17) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weight3), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //w4 - bottom input to top neuron
        double weight4 = SimpleNetworkController.weight4;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weight4), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight4), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos2+diameter2,(int) (y*1.05 + diameter2),xpos1,(int) (y*0.9 - diameter - 80), 70);
        //If weight 4 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 18 | stage == 19) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weight4), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //w5 - bottom input to middle neuron
        double weight5 = SimpleNetworkController.weight5;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weight5), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight5), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos2+diameter2,(int) (y*1.05 + diameter2),xpos1,y, 70);
        //If weight 5 is being adjusted, make background yellow to hightlight. Otherwise, keep as white
        if (stage == 20 | stage == 21) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weight5), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //w6 - bottom input to middle neuron
        double weight6 = SimpleNetworkController.weight6;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weight6), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weight6), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos2+diameter2,(int) (y*1.05 + diameter2),xpos1,(int) (y*1.1 + diameter+80), 70);
        //If weight 6 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 22 | stage == 23) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weight6), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        // Neuron to Output weights

        //weight output 3 - bottom neuron to output
        double weightOutput3 = SimpleNetworkController.weightOut3;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weightOutput3), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weightOutput3), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos1+diameter,(int) (y*1.1 + diameter+80),xpos3,y, 50);
        //If weight 6 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 29 | stage == 30) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weightOutput3), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //weight output 2 - middle neuron to output
        double weightOutput2 = SimpleNetworkController.weightOut2;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weightOutput2), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weightOutput2), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos1+diameter,y,xpos3,y, 50);
        //If weight 6 is being adjusted, make background yellow to hightlight. Otherwise, keep as white
        if (stage == 27 | stage == 28) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weightOutput2), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //weight output 1 - top neuron to output
        double weightOutput1 = SimpleNetworkController.weightOut1;
        g2.setFont(font);
        textWidth = (int) font.getStringBounds(Double.toString(weightOutput1), context).getWidth();
        ln = font.getLineMetrics(Double.toString(weightOutput1), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        //Calculate point along the line to display
        weightLoc = coordinate(xpos1+diameter,(int) (y*0.9 - diameter - 80),xpos3,y, 50);
        //If weight 6 is being adjusted, make background yellow to highlight. Otherwise, keep as white
        if (stage == 25 | stage == 26) {
            g2.setColor(Color.yellow);
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRect((int) (weightLoc[0]-(textWidth/2)-2), (int) (weightLoc[1]-(textHeight/2) - ln.getDescent()),textWidth+5,textHeight+5);
        g2.setColor(Color.black);
        //Draw weight
        g2.drawString(Double.toString(weightOutput1), (int) (weightLoc[0]-(textWidth/2)), (int) weightLoc[1]+(textHeight/2) - ln.getDescent());

        //in1
        double input1 = SimpleNetworkController.input1;
        textWidth = (int) font.getStringBounds(Double.toString(input1), context).getWidth();
        ln = font.getLineMetrics(Double.toString(input1), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.drawString(Double.toString(input1), xpos2 + diameter2/2 -(textWidth/2), (int) (y*1.05 + diameter2)+(textHeight/2) - ln.getDescent());

        //in2
        double input2 = SimpleNetworkController.input2;
        textWidth = (int) font.getStringBounds(Double.toString(input2), context).getWidth();
        ln = font.getLineMetrics(Double.toString(input2), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.setColor(Color.black);
        g2.drawString(Double.toString(input2), xpos2 + diameter2/2 -(textWidth/2), (int) (y*0.95 - diameter2)+(textHeight/2) - ln.getDescent());

        //out
        float output = (float) SimpleNetworkController.out;
        textWidth = (int) font.getStringBounds(Float.toString(output), context).getWidth();
        ln = font.getLineMetrics(Float.toString(output), context);
        textHeight = (int) (ln.getAscent() + ln.getDescent());
        g2.setColor(Color.black);
        g2.drawString(Float.toString(output), xpos3 + radius2-(textWidth/2), y+(textHeight/2) - ln.getDescent());


        //Add verbose details
        if (MainWindow.verbose) {

            Font font2 = new Font("Verdana", Font.PLAIN, ((dimension.width / 20)+(dimension.height/20))/3);
            g2.setFont(font2);

            //Summation Symbol for Middle Neuron
            //Adjust the scale
            int pixX = (int)Math.round((sum.getWidth() * (radius*0.0017)));
            int pixY = (int)Math.round((sum.getHeight() * (radius*0.0017)));
            //Get middle position
            int locX = xpos1+radius/2-(pixX/2);
            int locY = y-(pixY/2);
            //Summation symbol
            g.drawImage(sum, locX, locY, pixX, pixY, null);
            //Function symbol
            g.drawImage(function, locX+radius, locY, pixX, pixY, null);
            //Separator Line
            g.drawLine(xpos1+radius, y - radius, xpos1+radius, y+radius);

            //Summation Symbol for Top Neuron
            //Get middle position
            locY = (int) ((y*0.9)-(pixY/2) - diameter - 80);
            //Summation symbol
            g.drawImage(sum, locX, locY, pixX, pixY, null);
            //Function symbol
            g.drawImage(function, locX+radius, locY, pixX, pixY, null);
            //Separator Line
            g.drawLine(xpos1+radius, (int) (y*0.9 - radius - diameter - 80), xpos1+radius, (int) (y*0.9 + radius - diameter - 80));

            //Summation Symbol for Bottom Neuron
            //Get middle position
            locY = (int) ((y*1.1)-(pixY/2) + diameter + 80);
            //Summation symbol
            g.drawImage(sum, locX, locY, pixX, pixY, null);
            //Function symbol
            g.drawImage(function, locX+radius, locY, pixX, pixY, null);
            //Separator Line
            g.drawLine(xpos1+radius, (int) (y*1.1 - radius + diameter + 80), xpos1+radius, (int) (y*1.1 + radius + diameter + 80));

            //Summation Symbol for Output
            //Redraw node (position must be changed slightly)
            pixX = (int)Math.round((sum.getWidth() * (radius2*0.0017)));
            pixY = (int)Math.round((sum.getHeight() * (radius2*0.0017)));
            g.setColor(Color.decode("#c7e8ba"));
            g.fillOval(xpos3, y - radius2, diameter2, diameter2);
            g2.setColor(Color.BLACK);
            g2.drawOval(xpos3, y - radius2, diameter2, diameter2);
            //Get middle position
            locY = y-(pixY/2);
            //Summation symbol
            g.drawImage(sum, xpos3+radius2/2-(pixX/2), locY, pixX, pixY, null);
            //Function symbol
            g.drawImage(function, xpos3+radius2+radius2/2-(pixX/2), locY, pixX, pixY, null);
            //Separator Line
            g.drawLine(xpos3+radius2, y-radius2, xpos3+radius2, y+radius2);
            //Redraw output value in new position (below function symbol)
            output = (float) SimpleNetworkController.out;
            g2.setFont(font);
            textWidth = (int) font.getStringBounds(Float.toString(output), context).getWidth();
            ln = font.getLineMetrics(Float.toString(output), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(output), xpos3 + radius2 + (radius2/2)-(textWidth/2), y+(textHeight/2) + (radius2/2) + 2 - ln.getDescent());
            //Draw summation output
            float sum4 = (float) SimpleNetworkController.sum4;
            g2.setFont(font);
            textWidth = (int) font.getStringBounds(Float.toString(sum4), context).getWidth();
            ln = font.getLineMetrics(Float.toString(sum4), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(sum4), xpos3+radius2/2-(textWidth/2), y+(textHeight/2) + (radius2/2) + 2 - ln.getDescent());

            //Output sum + function values

            //Bottom neuron sum + function values
            //Sum value
            float sum3 = (float) SimpleNetworkController.sum3;
            textWidth = (int) font.getStringBounds(Float.toString(sum3), context).getWidth();
            ln = font.getLineMetrics(Float.toString(sum3), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(sum3), xpos1-(radius/2)+radius-(textWidth/2), (float) ((y*1.1 + diameter+80)+(textHeight/2) - ln.getDescent() + (radius/2) + 2));
            //Function value
            float function3 = (float) SimpleNetworkController.function3;
            textWidth = (int) font.getStringBounds(Float.toString(function3), context).getWidth();
            ln = font.getLineMetrics(Float.toString(function3), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(function3), xpos1+(radius/2)+radius-(textWidth/2), (float) ((y*1.1 + diameter+80)+(textHeight/2) - ln.getDescent() + (radius/2) + 2));

            //Middle neuron sum + function values
            //Sum value
            float sum2 = (float) SimpleNetworkController.sum2;
            textWidth = (int) font.getStringBounds(Float.toString(sum2), context).getWidth();
            ln = font.getLineMetrics(Float.toString(sum2), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(sum2), xpos1-(radius/2)+radius-(textWidth/2), y+(textHeight/2) - ln.getDescent() + (radius/2) + 2);
            //Function value
            float function2 = (float)SimpleNetworkController.function2;
            textWidth = (int) font.getStringBounds(Float.toString(function2), context).getWidth();
            ln = font.getLineMetrics(Float.toString(function2), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(function2), xpos1+(radius/2)+radius-(textWidth/2), y+(textHeight/2) - ln.getDescent() + (radius/2) + 2);

            //Top neuron sum + function values
            //Sum value
            float sum1 = (float) SimpleNetworkController.sum1;
            textWidth = (int) font.getStringBounds(Float.toString(sum1), context).getWidth();
            ln = font.getLineMetrics(Float.toString(sum1), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(sum1), xpos1-(radius/2)+radius-(textWidth/2), (float) ((y*0.9 - diameter - 80)+(textHeight/2) - ln.getDescent() + (radius/2) + 2));
            //Function value
            float function1 = (float) SimpleNetworkController.function1;
            textWidth = (int) font.getStringBounds(Float.toString(function1), context).getWidth();
            ln = font.getLineMetrics(Float.toString(function1), context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(Float.toString(function1), xpos1+(radius/2)+radius-(textWidth/2), (float) ((y*0.9 - diameter - 80)+(textHeight/2) - ln.getDescent() + (radius/2) + 2));

        }

        if (tipsEnabled) {
            //Tips for verbose mode
            Font font3 = new Font("TimesRoman", Font.PLAIN, ((dimension.width / 30)+(dimension.height/30))/3);
            g2.setFont(font3);
            context = g2.getFontRenderContext();
            String tip = SimpleNetworkController.tip;
            textWidth = (int) font3.getStringBounds(tip, context).getWidth();
            ln = font3.getLineMetrics(tip, context);
            textHeight = (int) (ln.getAscent() + ln.getDescent());
            g2.setColor(Color.black);
            g2.drawString(tip, dimension.width/2-(textWidth/2), (dimension.height)-(textHeight)- ln.getDescent());
        }

        // Summation
        if (stage == 3 | stage == 4 | stage == 5 | stage == 9 | stage == 33 | stage == 34 | stage == 35 | stage == 39) {
            //Adjust the scale
            int pixX = (int)Math.round((image.getWidth() * (radius*0.003)));
            int pixY = (int)Math.round((image.getHeight() * (radius*0.003)));
            //Get middle position
            int locX = (int) (xpos1-(pixX/4)+(getWidth()/3.1));
            int locY = y-(diameter*2)-(pixY/4);
            //Draw image
            g.drawImage(image, locX, locY, pixX, pixY, null);
        }

        // Activation
        if (stage == 6 | stage == 7 | stage == 8 | stage == 10 | stage == 36 | stage == 37 | stage == 38 | stage == 40) {
            if (Objects.equals(activation, "ReLu")) {
                //Adjust the scale
                int pixX = (int)Math.round((relu.getWidth() * (radius*0.009)));
                int pixY = (int)Math.round((relu.getHeight() * (radius*0.009)));
                //Get middle position
                int locX = (int) (xpos1-(pixX/4)+(getWidth()/3.5));
                int locY = y-(diameter*2)-(pixY/4);
                //Draw image
                g.drawImage(relu, locX, locY, pixX, pixY, null);
            } else if (Objects.equals(activation, "Sigmoid")) {
                //Adjust the scale
                int pixX = (int)Math.round((sigmoid.getWidth() * (radius*0.009)));
                int pixY = (int)Math.round((sigmoid.getHeight() * (radius*0.009)));
                //Get middle position
                int locX = (int) (xpos1-(pixX/4)+(getWidth()/3.5));
                int locY = y-(diameter*2)-(pixY/4);
                //Draw image
                g.drawImage(sigmoid, locX, locY, pixX, pixY, null);
            }

        }

        //First Layer Chain
        if (stage == 12 | stage == 14 | stage == 16 | stage == 18 | stage == 20 | stage == 22) {
            //Adjust the scale
            int pixX = (int)Math.round((chainFirst.getWidth() * (radius*0.004)));
            int pixY = (int)Math.round((chainFirst.getHeight() * (radius*0.004)));
            //Get middle position
            int locX = (int) (xpos1-(pixX/4)+(getWidth()/3.5));
            int locY = y-(diameter*2)-(pixY/4);
            //Draw image
            g.drawImage(chainFirst, locX, locY, pixX, pixY, null);
        } else if (stage == 13 | stage == 15 | stage == 17 | stage == 19 | stage == 21 | stage == 23 | stage == 26 | stage == 28 | stage == 30) {
            //Adjust the scale
            int pixX = (int)Math.round((weightAdjustment.getWidth() * (radius*0.007)));
            int pixY = (int)Math.round((weightAdjustment.getHeight() * (radius*0.007)));
            //Get middle position
            int locX = (int) (xpos1-(pixX/4)+(getWidth()/3.5));
            int locY = y-(diameter*2)-(pixY/4);
            //Draw image
            g.drawImage(weightAdjustment, locX, locY, pixX, pixY, null);
        } else if (stage == 25 | stage == 27 | stage == 29) {
            //Compute chain rule for second layer weight steps
            //Adjust the scale
            int pixX = (int)Math.round((chainSecond.getWidth() * (radius*0.004)));
            int pixY = (int)Math.round((chainSecond.getHeight() * (radius*0.004)));
            //Get middle position
            int locX = (int) (xpos1-(pixX/4)+(getWidth()/3.5));
            int locY = y-(diameter*2)-(pixY/4);
            //Draw image
            g.drawImage(chainSecond, locX, locY, pixX, pixY, null);
        }

        if (dataTableEnabled){
            //Create new instance of the DataTable class
            DataTable table = new DataTable();

            //Compute desired size and position of the table for this template
            int xsize = Math.round(dimension.width/2);
            int xmid = (dimension.width/2)-(xsize/2);
            int ysize = (int)Math.round((dimension.height/2)*0.18);

            //Call the draw function in order to draw the table
            table.paint(g,xmid,ysize-40, xsize, ysize);
        }

    }

}

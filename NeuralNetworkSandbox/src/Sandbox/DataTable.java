package Sandbox;
import Interface.MainWindow;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.util.List;
import java.util.Objects;


public class DataTable {

    //Use this to go through rows! Only 2 rows are shown at a time. By default, it will show the first and second from the data set. rowOffset = 2 will show rows 3 and 4.
    //For future reference, this offset should be the 'row' variable from PerceptronController. This means when the perceptron does a new row, the data table will also move a row down
    public static int rowOffset = 0;

    public void paint (Graphics g, int xpos, int ypos, int width, int height){

        if (!MainWindow.dataComplete) {
            //Check to make sure the data isn't finished with
            //If it is, then there is no next data to display so don't draw the table
            //First draw outline
            g.drawRect(xpos,ypos,width,height);

            //Draw title
            Font font = new Font("TimesRoman", Font.PLAIN, 13);
            g.setFont(font);
            Graphics2D g2 = (Graphics2D) g;
            FontRenderContext context;
            context = g2.getFontRenderContext();
            String tip = "Data Table";
            font.getLineMetrics(tip, context);
            LineMetrics ln;
            int textWidth;
            ln = font.getLineMetrics(tip, context);
            int textHeight;
            g2.setColor(Color.black);
            g2.drawString(tip, xpos, ypos - ln.getDescent());

            //First sep line
            int splitSize = height/3;
            g.drawLine(xpos,ypos+splitSize,xpos+width,ypos+splitSize);
            //Second sep line
            g.drawLine(xpos,ypos+(splitSize*2),xpos+width,ypos+(splitSize*2));

            //The number of columns in the data table should be dependent on the number of inputs from the used dataset
            if (Objects.equals(MainWindow.dataSetFile, "test.csv")) {

                List<List<String>> data = DataFeed.data("test.csv");
                MainWindow.sampleCount = data.size();
                System.out.println("size is "+MainWindow.sampleCount);

                int widthSplit = (width / 4);
                //font = new Font("TimesRoman", Font.PLAIN, 13);
                font = new Font("TimesRoman", Font.PLAIN, (widthSplit/10)+(height/14));
                g.setFont(font);

                //First
                g.drawLine(xpos+widthSplit,ypos+height,xpos+widthSplit,ypos);
                //Second
                g.drawLine(xpos+(widthSplit*2),ypos+height,xpos+(widthSplit*2),ypos);
                //Third
                g.drawLine(xpos+(widthSplit*3),ypos+height,xpos+(widthSplit*3),ypos);

                //Adjusted split
                int widthOffset = (int) (widthSplit*0.5);

                //-------Populate Table
                //Title column 1
                String text = "Input 1";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, xpos+widthOffset-(textWidth/2), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1, Cell 1
                //Retrieve value
                List<String> row = data.get(rowOffset);
                text = row.get(0);

                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, xpos+widthOffset-(textWidth/2), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2, Cell 1
                List<String> row2 = data.get(1+rowOffset);
                text = row2.get(0);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, xpos+widthOffset-(textWidth/2), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

                //Title column 2
                text = "Input 2";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*3)-(textWidth/2)), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1, Cell 2
                text = row.get(1);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*3)-(textWidth/2)), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2, Cell 2
                text = row2.get(1);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*3)-(textWidth/2)), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

                //Title column 3
                text = "Input 3";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*5)-(textWidth/2)), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1, Cell 3
                text = row.get(2);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*5)-(textWidth/2)), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2, Cell 3
                text = row2.get(2);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*5)-(textWidth/2)), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

                //Title column output
                text = "Desired Output";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*7)-(textWidth/2)), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1 output
                text = row.get(3);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*7)-(textWidth/2)), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2 output
                text = row2.get(3);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*7)-(textWidth/2)), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

            }

            if (Objects.equals(MainWindow.dataSetFile, "test2.csv")) {

                List<List<String>> data = DataFeed.data("test2.csv");

                int widthSplit = (width / 3);
                //font = new Font("TimesRoman", Font.PLAIN, 13);
                font = new Font("TimesRoman", Font.PLAIN, (widthSplit/10)+(height/14));
                g.setFont(font);

                //First
                g.drawLine(xpos+widthSplit,ypos+height,xpos+widthSplit,ypos);
                //Second
                g.drawLine(xpos+(widthSplit*2),ypos+height,xpos+(widthSplit*2),ypos);

                //Adjusted split
                int widthOffset = (int) (widthSplit*0.5);

                //-------Populate Table
                //Title column 1
                String text = "Input 1";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, xpos+widthOffset-(textWidth/2), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1, Cell 1
                //Retrieve value
                List<String> row = data.get(rowOffset);
                text = row.get(0);

                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, xpos+widthOffset-(textWidth/2), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2, Cell 1
                List<String> row2 = data.get(1+rowOffset);
                text = row2.get(0);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, xpos+widthOffset-(textWidth/2), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

                //Title column 2
                text = "Input 2";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*3)-(textWidth/2)), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1, Cell 2
                text = row.get(1);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*3)-(textWidth/2)), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2, Cell 2
                text = row2.get(1);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*3)-(textWidth/2)), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

                //Title column 3
                text = "Desired Output";
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*5)-(textWidth/2)), (float) (ypos+(splitSize*0.5) +(textHeight/2)- ln.getDescent()));

                //Row 1, Cell 3
                text = row.get(2);
                System.out.println(text);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*5)-(textWidth/2)), (float) (ypos+(splitSize*1.5) +(textHeight/2)- ln.getDescent()));

                //Row 2, Cell 3
                text = row2.get(2);
                textWidth = (int) font.getStringBounds(text, context).getWidth();
                ln = font.getLineMetrics(text, context);
                textHeight = (int) (ln.getAscent() + ln.getDescent());
                g2.drawString(text, (float) (xpos+(widthOffset*5)-(textWidth/2)), (float) (ypos+(splitSize*2.5) +(textHeight/2)- ln.getDescent()));

            }

        }
    }


}

package eecs1021;

import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.Pin;
import org.firmata4j.firmata.*;
import org.firmata4j.IODevice;
import org.firmata4j.I2CDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class MainClass {
    public static void main(String[] args) throws IOException, InterruptedException {
        String port = "COM4";
        IODevice myGroveBoard = new FirmataDevice(port);
        try {
            myGroveBoard.start();
            System.out.println("Board started");
            myGroveBoard.ensureInitializationIsDone();
        }
        catch(Exception ex) {
            System.out.println("Couldn't connect to board");
        }
        finally {
            Pin mySoilSensor = myGroveBoard.getPin(15);
            mySoilSensor.setMode(Pin.Mode.ANALOG);
            Pin myPump = myGroveBoard.getPin(7);
            myPump.setMode(Pin.Mode.OUTPUT);
            Pin myButton = myGroveBoard.getPin(6);
            myButton.setMode(Pin.Mode.INPUT);

            int j = 0;
            ArrayList<Long> data = new ArrayList<>();

            I2CDevice i2CDevice = myGroveBoard.getI2CDevice((byte) 0x3C);
            SSD1306 myOledObject = new SSD1306(i2CDevice, SSD1306.Size.SSD1306_128_64);
            myOledObject.init();
            myOledObject.getCanvas().clear();
            myOledObject.display();

            // create the window
            StdDraw.setXscale(-20, 105);
            StdDraw.setYscale(-1, 6);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(0,0,0,5); //y-axis
            StdDraw.line(0, 0, 100, 0); // x-axis
            StdDraw.text(50, 5.5, "Sensor Voltage vs. Time");//title
            StdDraw.text(50, -0.5,"Time (sec)");// x-axis label
            StdDraw.text(-15,2.5,"Sensor"); // y-axis label
            StdDraw.text(-15, 2.2, "Voltage");// y-axis label
            StdDraw.text(-15, 1.9, "Values");// y-axis label
            double y = -0.2;
            for (int i=0; i<6;i++) { // y-axis
                StdDraw.text(-2, y, String.valueOf(i));
                y++;
            }
            int x = 10;
            for(int i = 1; i < 11; i++) { // x-axis
                StdDraw.text(x, -0.2, String.valueOf(i));
                x+=10;
            }
            StdDraw.setPenColor(StdDraw.BOOK_RED);//set colour of data points


            ButtonListener myButtonListener = new ButtonListener(myButton, mySoilSensor, myPump, data);
            myGroveBoard.addEventListener(myButtonListener);// runs when button is pressed
            var task = new PumpTask(mySoilSensor, myPump, data, j, myOledObject);
            new Timer().schedule(task, 0, 1000); // runs every minute

        }
    }
}

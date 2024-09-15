package eecs1021;

import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class PumpTask extends TimerTask {
    private final Pin pump;
    private final SSD1306 myOledObject;
    private final Pin soilSensor;
    ArrayList<Long> data;
    int i;
    public PumpTask(Pin mySoilSensor, Pin myPump, ArrayList<Long> data, int i, SSD1306 myOledObject) {
        pump = myPump;
        soilSensor = mySoilSensor;
        this.data = data;
        this.i = i;
        this.myOledObject = myOledObject;
    }
    @Override
    public void run() {
        data.add(soilSensor.getValue());
        i++;
        if (i < 10) {
            for (int k = 0; k < i; k++) {
                StdDraw.text(k * 10, data.get(k) / 204.6, "*");
            }
        }
        if (soilSensor.getValue() < 600) {
            System.out.println(soilSensor.getValue());
            try {
                pump.setValue(0);
                Thread.sleep(2000);
                myOledObject.getCanvas().clear();
                myOledObject.getCanvas().setTextsize(1);
                myOledObject.getCanvas().drawString(0,0,"Pump off\nSoil is wet");
                myOledObject.display();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else if (soilSensor.getValue() < 700) {
            System.out.println(soilSensor.getValue());
            try {
                pump.setValue(1);
                Thread.sleep(1000);
                pump.setValue(0);
                Thread.sleep(1000);
                myOledObject.getCanvas().clear();
                myOledObject.getCanvas().setTextsize(1);
                myOledObject.getCanvas().drawString(0,0,"Pump on\nSoil is only\npartially wet");
                myOledObject.display();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else if (soilSensor.getValue() < 1024) {
            System.out.println(soilSensor.getValue());
            try {
                pump.setValue(1);
                Thread.sleep(1000);
                pump.setValue(0);
                Thread.sleep(1000);
                myOledObject.getCanvas().clear();
                myOledObject.getCanvas().setTextsize(1);
                myOledObject.getCanvas().drawString(0,0,"Pump on\nSoil is very dry");
                myOledObject.display();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

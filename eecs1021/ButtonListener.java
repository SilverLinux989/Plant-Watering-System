package eecs1021;

import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;

import java.io.IOException;
import java.util.ArrayList;

public class ButtonListener implements IODeviceEventListener {
    Pin Button;
    Pin SoilSensor;
    Pin pump;
    ArrayList<Long> data;
    public ButtonListener(Pin myButton, Pin mySoilSensor, Pin myPump, ArrayList<Long> data) {
        Button = myButton;
        SoilSensor = mySoilSensor;
        pump = myPump;
        this.data = data;
    }
    @Override
    public void onStart(IOEvent event) {}
    @Override
    public void onStop(IOEvent event) {}
    @Override
    public void onMessageReceive(IOEvent event, String msg) {}
    @Override
    public void onPinChange(IOEvent event){
        if (event.getPin().getIndex() != Button.getIndex()){
            return;
        }
        else {
            try {
                pump.setValue(1);
                Thread.sleep(1000);
                pump.setValue(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package eecs1021;
import org.firmata4j.I2CDevice;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.*;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ButtonTester {
    @Test
    public void test() throws IOException, InterruptedException {
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
            I2CDevice i2cDevice = myGroveBoard.getI2CDevice((byte)0x3C);
            SSD1306 myOled = new SSD1306(i2cDevice, SSD1306.Size.SSD1306_128_64);
            myOled.init();
            int i = 0;
            ArrayList<Long> data = new ArrayList<>();
            PumpTask PumpTask = new PumpTask(mySoilSensor, myPump, data, i, myOled);
            PumpTask.run();
            assertEquals(0, myPump.getValue());
        }
    }
}

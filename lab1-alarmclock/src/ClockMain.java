import java.util.concurrent.Semaphore;

import clock.ClockInput;
import clock.ClockInput.UserInput;
import clock.ClockOutput;
import emulator.AlarmClockEmulator;
import klocka;

public class ClockMain {
	
	public static void clock(klocka k, ClockOutput out){
		while (true) {
            	try {
            		long t1 = System.currentTimeMillis();
            		k.tick(out);
            		Thread.sleep(1000-(System.currentTimeMillis()-t1));
            	}catch (InterruptedException e) {
                    throw new Error(e);
            	}
            }

		
	}
	

    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();
        klocka c = new  klocka();

        ClockInput  in  = emulator.getInput();
        ClockOutput out = emulator.getOutput();

        Semaphore sem = in.getSemaphore();
        Thread  klocktråd = new  Thread (() -> clock(c, out));
        klocktråd.start();
        while (true) {
            sem.acquire(); 
            // wait for user input
            UserInput userInput = in.getUserInput();
            int choice = userInput.getChoice();
            int value = userInput.getValue();
            
            switch(choice) {
            	case 1:
            		c.setTime(value);
            		break;
            	case 2:
            		c.setAlarmTime(value);
            		break;
            	case 3:
            		c.setAlarm(out);
                	break;
            	case 4:
            		c.silenceAlarm();
            		break;
            }
            System.out.println("choice = " + choice + "  value=" + value);

        }
    }
}

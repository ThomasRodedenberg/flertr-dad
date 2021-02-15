package lab;
import simulator.WashingSimulator;
import wash.WashingIO;

public class Wash {

    // simulation speed-up factor:
    // 50 means the simulation is 50 times faster than real time
    public static final int SPEEDUP = 50;

    public static void main(String[] args) throws InterruptedException {
        WashingSimulator sim = new WashingSimulator(SPEEDUP);
        
        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);
        
        //gemensam tr�d ?
        MessagingThread<WashingMessage> tr�d = null;

        temp.start();
        water.start();
        spin.start();

        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);
            
            switch(n) {
            case 0:
            	tr�d.interrupt(); 
            	break;
            case 1:
            	tr�d = new WashingProgram1(io,temp,water,spin);
            	tr�d.start();
            	break;
            case 2:
            	tr�d = new WashingProgram2(io,temp,water,spin);
            	tr�d.start();
            	break;
            case 3:
            	tr�d = new WashingProgram3(io,temp,water,spin);
            	tr�d.start();
            	break;
            default:
            	
            }
            // TODO:
            // if the user presses buttons 1-3, start a washing program
            // if the user presses button 0, and a program has been started, stop it
        }
    }
};

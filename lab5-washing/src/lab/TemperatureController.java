package lab;

import wash.WashingIO;

public class TemperatureController extends MessagingThread<WashingMessage> {

    // TODO: add attributes
	private WashingIO io; 
	private double lastValue = 0;
	private int lastCommand = 4;
	private MessagingThread<WashingMessage> lastSender;

    public TemperatureController(WashingIO io) {
        // TODO
    	this.io = io;
    }

    @Override
    public void run() {
    	try {
    		// ... TODO ...

    		while (true) {

    			WashingMessage m = receiveWithTimeout(10000 / Wash.SPEEDUP);
    			if (m != null) {
    				lastCommand = m.getCommand();
    				lastValue = 0;
    				lastSender = m.getSender();
    				
    				if(lastCommand == WashingMessage.TEMP_SET) {
    					lastValue = m.getValue();
    				}
    				System.out.println("got " + m);
    			}
    			if(io.getTemperature()<=lastValue - 1.99 && io.getWaterLevel() != 0) {
					io.heat(true);
				}else if(io.getTemperature()>=lastValue - 0.478){
					io.heat(false);
					if (lastSender != null) {
						WashingMessage ack = new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT);
						lastSender.send(ack);
						lastSender = null;
					}
				}
    			

    		}
        } catch (InterruptedException unexpected) {

        	System.out.println("TempController interrupted!");
            throw new Error(unexpected);
        }
        
    }
}

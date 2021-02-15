package lab;

import wash.WashingIO;

public class WaterController extends MessagingThread<WashingMessage> {

	// TODO: add attributes
	private WashingIO io; 
	private int lastCommand = 6;
	private double lastValue = 0;
	private MessagingThread<WashingMessage> lastSender;

	public WaterController(WashingIO io) {
		// TODO
		this.io = io;
	}

	@Override
	public void run() {
		try {
			// ... TODO ...

			while (true) {

				WashingMessage m = receiveWithTimeout(2500 / Wash.SPEEDUP);
				if (m != null) {
					lastCommand = m.getCommand();
					lastSender = m.getSender();
					if (lastCommand == WashingMessage.WATER_FILL) {
						io.drain(false);
						lastValue = m.getValue();
					}else if(lastCommand == WashingMessage.WATER_DRAIN) {
						io.fill(false);
						lastValue = 0;
					}
					System.out.println("got " + m);
				} 

				if(lastCommand == WashingMessage.WATER_FILL) {
//					io.drain(false);
					if(io.getWaterLevel()<lastValue) {
						io.fill(true);
					}else if (lastSender != null){
						io.fill(false);
						WashingMessage ack = new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT);
						lastSender.send(ack);
						lastSender = null;
					}
				}
				if(lastCommand == WashingMessage.WATER_DRAIN) {
//					io.fill(false);
					if(io.getWaterLevel()>0) {
						io.drain(true);						
					}else if(lastSender != null){
						io.drain(false);
						WashingMessage ack = new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT);
						lastSender.send(ack);
						lastSender = null;
					}
				}
				if(lastCommand == WashingMessage.WATER_IDLE) {
					io.fill(false);
					io.drain(false);
				}

			}
		} catch (InterruptedException unexpected) {

			System.out.println("WaterController interrupted!");
			throw new Error(unexpected);
		}

	}
}

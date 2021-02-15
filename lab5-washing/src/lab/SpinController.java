package lab;

import wash.WashingIO;

public class SpinController extends MessagingThread<WashingMessage> {

    // TODO: add attributes
	private WashingIO io;
	private boolean right = false;
	private int lastCommand = 1;

    public SpinController(WashingIO io) {
        // TODO
    	this.io = io;
    }

    @Override
    public void run() {
        try {
            // ... TODO ...

            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Wash.SPEEDUP);
                
                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                	lastCommand = m.getCommand();
                	System.out.println("got " + m);
                } 
                switch(lastCommand) {
                case 1:
                	io.setSpinMode(1);
                	break;
                case 2:
                	if(!right) {
                		io.setSpinMode(2);
                		right =!right;
                	}else{
                		io.setSpinMode(3);
                		right =!right;
                	}
                	break;
                case 3:
                	if(io.getWaterLevel() == 0) {
                		io.setSpinMode(4);                		
                	}
                	break;
                default:

                }

            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
        	System.out.println("SpinController interrupted!");
            throw new Error(unexpected);
        }
    }
}

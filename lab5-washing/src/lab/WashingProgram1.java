package lab;

import wash.WashingIO;

public class WashingProgram1 extends MessagingThread<WashingMessage> {
	
	private WashingIO io;
    private MessagingThread<WashingMessage> temp;
    private MessagingThread<WashingMessage> water;
    private MessagingThread<WashingMessage> spin;
    
    public WashingProgram1(WashingIO io,
                           MessagingThread<WashingMessage> temp,
                           MessagingThread<WashingMessage> water,
                           MessagingThread<WashingMessage> spin) {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }

    @Override
    public void run() {
        try {
            System.out.println("washing program 1 started");
            
            io.lock(true);
            water.send(new WashingMessage(this, WashingMessage.WATER_FILL, 10));
            WashingMessage ack = receive();  // wait for acknowledgment
            System.out.println("got " + ack);

            spin.send(new WashingMessage(this, WashingMessage.SPIN_SLOW));
            
            temp.send(new WashingMessage(this, WashingMessage.TEMP_SET,40));
            ack = receive();  // wait for acknowledgment
            System.out.println("got " + ack);
            
            
            Thread.sleep(30 * 60000 / Wash.SPEEDUP);
            
            
            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            receive();
            
            for(int i=0;i<5;i++) {
            	water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
                ack = receive();  // wait for acknowledgment
                System.out.println("got " + ack);
            	water.send(new WashingMessage(this, WashingMessage.WATER_FILL, 10));
                receive();
            }
            water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
            receive();
            
            spin.send(new WashingMessage(this, WashingMessage.SPIN_FAST));
            Thread.sleep(5 * 60000 / Wash.SPEEDUP);
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            
            // Unlock hatch
            io.lock(false);
            
            System.out.println("washing program 1 finished");
        } catch (InterruptedException e) {
            
            // if we end up here, it means the program was interrupt()'ed
            // set all controllers to idle

            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            System.out.println("washing program terminated");
        }
    }
}

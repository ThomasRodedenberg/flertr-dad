package factory.controller;


import java.util.concurrent.Semaphore;

import factory.model.DigitalSignal;
import factory.model.WidgetKind;
import factory.swingview.Factory;

public class ToolController {
    private final DigitalSignal conveyor, press, paint;
    private final long pressingMillis, paintingMillis;
    private Semaphore pressnere = new Semaphore(1);
    private Semaphore målar = new Semaphore(1);
    
    public ToolController(DigitalSignal conveyor,
                          DigitalSignal press,
                          DigitalSignal paint,
                          long pressingMillis,
                          long paintingMillis)
    {
        this.conveyor = conveyor;
        this.press = press;
        this.paint = paint;
        this.pressingMillis = pressingMillis;
        this.paintingMillis = paintingMillis;
        
    }

    public void onPressSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
    	
        if (widgetKind == WidgetKind.BLUE_RECTANGULAR_WIDGET) {
        	pressnere.acquire();
            conveyor.off();
            press.on();
            Thread.sleep(pressingMillis);
            press.off();
            Thread.sleep(pressingMillis);   // press needs this time to retract
            pressnere.release();
            conveyorOn();
        }
    }

    public void onPaintSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
        if (widgetKind == WidgetKind.ORANGE_ROUND_WIDGET) {
        	// TODO
        	målar.acquire();
        	conveyor.off();
            paint.on();
            Thread.sleep(paintingMillis);
            paint.off();
            Thread.sleep(paintingMillis);
            målar.release();
            conveyorOn();
        }
    }
    
    public synchronized void conveyorOn() throws InterruptedException {
    	pressnere.acquire();
    	målar.acquire();
    	conveyor.on();
    	pressnere.release();
    	målar.release();
    }
    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.startSimulation();
    }
}


package factory.controller;


import factory.model.DigitalSignal;
import factory.model.WidgetKind;
import factory.swingview.Factory;

public class ToolController {
    private final DigitalSignal conveyor, press, paint;
    private final long pressingMillis, paintingMillis;
    private boolean pressnere;
    
    public ToolController(DigitalSignal conveyor,
                          DigitalSignal press,
                          DigitalSignal paint,
                          long pressingMillis,
                          long paintingMillis)
    {
        this.conveyor = conveyor;
        this.press = press;
        this.paint = paint;
        this.pressingMillis = pressingMillis;
        this.paintingMillis = paintingMillis;
        
    }

    public void onPressSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
    	
        if (widgetKind == WidgetKind.BLUE_RECTANGULAR_WIDGET) {
        	pressNere();
            conveyor.off();
            press.on();
            Thread.sleep(pressingMillis);
            press.off();
            Thread.sleep(pressingMillis);   // press needs this time to retract
            pressUppe();
            conveyorOn();
        }
    }

    public void onPaintSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
        if (widgetKind == WidgetKind.ORANGE_ROUND_WIDGET) {
        	// TODO
        	conveyor.off();
            paint.on();
            Thread.sleep(paintingMillis);
            paint.off();
            Thread.sleep(paintingMillis);
            conveyorOn();
        }
    }
    
    public synchronized void conveyorOn() throws InterruptedException {
    	while(pressnere) {
    		wait();
    	}
    	conveyor.on();
    }
    public synchronized void pressNere() {
    	pressnere = true;
    	
    }
    public synchronized void pressUppe() {
    	pressnere = false;
    	notifyAll();
    }

    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.startSimulation();
    }
}

package factory.controller;


import factory.model.DigitalSignal;
import factory.model.WidgetKind;
import factory.swingview.Factory;

public class ToolController {
    private final DigitalSignal conveyor, press, paint;
    private final long pressingMillis, paintingMillis;
    private boolean pressnere;

    
    public ToolController(DigitalSignal conveyor,
                          DigitalSignal press,
                          DigitalSignal paint,
                          long pressingMillis,
                          long paintingMillis)
    {
        this.conveyor = conveyor;
        this.press = press;
        this.paint = paint;
        this.pressingMillis = pressingMillis;
        this.paintingMillis = paintingMillis;
        
    }

    public synchronized void onPressSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
    	
        if (widgetKind == WidgetKind.BLUE_RECTANGULAR_WIDGET) {
        	pressnere = true;
            conveyor.off();
            press.on();
            waitOutside(pressingMillis);
            press.off();
            waitOutside(pressingMillis);   // press needs this time to retract
            conveyor.on();
        }
    }

    public synchronized void onPaintSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
        if (widgetKind == WidgetKind.ORANGE_ROUND_WIDGET) {
        	// TODO
        	conveyor.off();
            paint.on();
            waitOutside(paintingMillis);
            paint.off();
            conveyor.on();
        }
    }
    private void waitOutside(long t) throws InterruptedException{
    	long timeToWakeUp = System.currentTimeMillis() + t;
    	//
    	while(System.currentTimeMillis() < timeToWakeUp) {
    		long dt = timeToWakeUp - System.currentTimeMillis();
    		wait(dt);
    	}
    }


    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.startSimulation();
    }
}

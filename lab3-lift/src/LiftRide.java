import lift.LiftView;
import lift.Passenger;

public class LiftRide {
	
	public static void lift(LiftView view, liftMonitor m) {
		while(true) {
			try {
				if(!m.isEmpty()) {
					m.setUp();
					if (m.getUp()) {
						m.moving();
						view.moveLift(m.getHere(), m.getHere()+1);
						m.setHere(m.getHere()+1);
						m.notMoving();
					} else if (!m.getUp()) {
						m.moving();
						view.moveLift(m.getHere(), m.getHere()-1);
						m.setHere(m.getHere()- 1);
						m.notMoving();	
					}
					m.liftWaits();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	 public static void kör(LiftView view, liftMonitor m){
		 	
		 	while(true) {
		 		Passenger passenger = view.createPassenger();
		 		try {
					Thread.sleep((long)(Math.random() * 45000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 		//int from = passenger.getStartFloor();
		 		//int to   = passenger.getDestinationFloor();
		 	
		 		

		 		passenger.begin();           

		 		try {
		 			m.waitEntry(passenger);
					m.enter(passenger);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
		 		try {
					m.exit(passenger);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}          
		 		passenger.end();               
		 	}
	 }
	 public static void main(String[] args) {
		 LiftView view = new LiftView();
		 liftMonitor m = new liftMonitor(view);
		 Thread lift = new Thread (() -> lift(view, m));
		 lift.start();
		 for (int i = 0; i<20;i++) {
			 Thread p = new Thread (() -> kör(view, m));
			 p.start();
		 }
		 
	 }

}

import lift.LiftView;
import lift.Passenger;

public class liftMonitor {
	private LiftView view;
	private int here;        	// If here!=next, here (floor number) tells from which floor
								// the lift is moving and next to which floor it is moving.
	private boolean moving;							
	private int[] waitEntry = new int[7]; 	// Number of passengers waiting to enter the lift at the
								// various floors.
	private int[] waitExit = new int[7];  	// Number of passengers (inside the lift) waiting to leave
								// the lift at the various floors.
	private int load;        	// Number of passengers currently in the lift.
//	private int[] waitUp = new int[7];
//	private int[] waitDown = new int[7];
	private boolean up;
	
	public liftMonitor(LiftView v) {
		here = 0;
		load = 0;
		view = v;
		up=true;
		moving = false;
		
	}

//	public synchronized void moveLift() throws InterruptedException {
//		if(!isEmpty()) {
//			if (here == 6 || notUp(here)) {
//				up = false;
//			}else if (here == 0 || notDown(here)) {
//				up = true;
//			}
//			if (up) {
//				next = here +1;
//				view.moveLift(here, next);
//				here = next;
//			} else if (!up) {
//				next = here -1;
//				view.moveLift(here, next);	
//				here = next;
//			}
//			notifyAll();
//			wait(10);
//			while ((waitEntry[here] != 0 && load < 4 )|| waitExit[here] != 0 ) {
//				wait();
//			}
//		}
//	}
	public synchronized void enter(Passenger p) throws InterruptedException {
//		waitEntry[p.getStartFloor()]++;
		
//		if(p.getDestinationFloor() >= p.getStartFloor()) {
//			waitUp[p.getStartFloor()]++;
//		}else if(p.getDestinationFloor() >= p.getStartFloor()) {
//			waitDown[p.getStartFloor()]++;
//		}

		while (p.getStartFloor() != here || load >= 4 || moving) {// || (p.getDestinationFloor()<here && up) || (p.getDestinationFloor()>here && !up)) {
			wait();
		}
		p.enterLift();
		waitEntry[p.getStartFloor()]--;
		load++;
		waitExit[p.getDestinationFloor()]++;
		notifyAll();
	}
	public synchronized void exit(Passenger p) throws InterruptedException {
		while (p.getDestinationFloor() != here || moving) {
			wait();
		}
		p.exitLift();
		waitExit[here]--;
		load--;
		notifyAll();	
	}	
	public synchronized boolean isEmpty() {
		if((waitEntry[0] == 0 && waitEntry[1] == 0 && waitEntry[2] == 0 && waitEntry[3] == 0 && waitEntry[4] == 0 && waitEntry[5] == 0 && waitEntry[6] == 0) && load ==0) {
			return true;
		}
		return false;
	}
	private synchronized boolean notUp(int n) {
		boolean retur = false;
			for(int i = n; i<7;i++) {
				if (waitEntry[i]==0 && waitExit[i]==0) {
					retur=true;
				}else {
					return false;
				}
			}
		return retur;
	}
	private synchronized boolean notDown(int n) {
		boolean retur = false;
			for(int i = n; i>=0;i--) {
				if (waitEntry[i]==0 && waitExit[i]==0) {
					retur=true;
				}else {
					return false;
				}
			}
		return retur;
	}
	public synchronized int getHere() {
		return here;
	}
	public synchronized boolean getUp() {
		return up;
	}

	public synchronized void setHere(int i) {
		here = i;
	}
	public synchronized void setUp() {
		if (here == 6 || notUp(here)) {
			up = false;
		}else if (here == 0 || notDown(here)) {
			up = true;
		}
	}

	public synchronized void liftWaits() throws InterruptedException {
		notifyAll();
		while ((waitEntry[here] != 0 && load < 4 ) || waitExit[here] != 0) {
			wait();
		}
	}
	public synchronized void moving() {
		moving =true;
	}
	public synchronized void notMoving() {
		moving =false;
	}
	public synchronized void waitEntry(Passenger p) {
		waitEntry[p.getStartFloor()]++;
		notifyAll();
	}

}
import java.util.concurrent.Semaphore;

import clock.ClockInput;
import clock.ClockInput.UserInput;
import clock.ClockOutput;
import emulator.AlarmClockEmulator;

public class klocka {
		private int s;
		private int m;
		private int h;
		private int alarmTime;
		private boolean alarm;
		private Semaphore mutex = new Semaphore(1);
		private int alarmTick;
		
		public klocka() {
			s = 0;
			m = 0;
			h = 0;
			alarmTime = 0;
			alarm = false;
			alarmTick=0;
		}
		
		private int getTime() {
			return h*10000+ m*100 +s;
		}
		
		void setTime(int tid){
			try {
				mutex.acquire();
				s = tid%100;
				m = ((tid%10000)-s)/100;
				h = ((tid%1000000)-m-s)/10000;
				mutex.release();
			 } catch (InterruptedException e) {
		            throw new Error(e);
		        }

		}
		
		void tick(ClockOutput out) {
			try {
			mutex.acquire();
			s = (s+1)%60;
			if (s==0) {
				m = (m+1)%60;
				if (m==0) {
					h = (h+1)%24;
				}
			} 
			out.displayTime(getTime());
			if((getTime() == alarmTime && alarm) || alarmTick > 0) { 
       	 		out.alarm();
       	 		alarmTick++;
       	 		if (alarmTick >= 21) {
       	 			alarmTick=0;
       	 		}		              	 		
       	 	}
			mutex.release();
			} catch (InterruptedException e) {
	            throw new Error(e);
	        }
			
		}
		
		void setAlarmTime(int tid) {
			try {
				mutex.acquire();
				alarmTime = tid;
				mutex.release();
			}catch (InterruptedException e) {
	            throw new Error(e);
	        }	
		}
		
		void setAlarm(ClockOutput out) {
			try {
				mutex.acquire();
				alarm = !alarm;
				out.setAlarmIndicator(alarm);
				mutex.release();
			}catch (InterruptedException e) {
				throw new Error(e);
			}
		}
		
		void silenceAlarm() {
			try {
				mutex.acquire();
				alarmTick = 0;
				mutex.release();
			}catch (InterruptedException e) {
				throw new Error(e);
			}
		}
			
}

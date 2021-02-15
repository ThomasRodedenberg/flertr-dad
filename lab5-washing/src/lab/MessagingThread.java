package lab;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class MessagingThread<M> extends Thread {
	private BlockingQueue<M> queue = new LinkedBlockingDeque<>();

    /** Called by another thread, to send a message to this thread. */
    public void send(M message) {
        // TODO: implement this method (one or a few lines)
    	queue.offer(message);
    }
    
    /** Returns the first message in the queue, or blocks if none available. */
    protected M receive() throws InterruptedException {
        // TODO: implement this method (one or a few lines)
        return queue.take();
    }
    
    /** Returns the first message in the queue, or blocks up to 'timeout'
        milliseconds if none available. Returns null if no message is obtained
        within 'timeout' milliseconds. */
    protected M receiveWithTimeout(long timeout) throws InterruptedException {
        // TODO: implement this method (one or a few lines)
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }
}
package edu.monash.knowledgezoo.api.utils;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * execute code in another thread. support delayed execution.
 * @author shin
 *
 */
public class Looper {
	private final DelayQueue<Message> msgQueue = new DelayQueue<>();
	private Thread thread;
	private Dispatcher dispatcher = new Dispatcher() {
		@Override
		public void dispatch(int code, Object param) {}
	};

	/**
	 * start a thread to handle posted codes.
	 */
	public Looper start() {
		if (thread != null)
			throw new IllegalStateException("already started");
		thread = new LooperThread();
    	thread.start();
    	msgQueue.clear();
    	return this;
	}
	
	public void stop() {
		if (thread == null)
			throw new IllegalStateException("already stopped");
		thread.interrupt();
		thread = null;
	}
	
	public Looper setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		return this;
	}
	
	public void post(Runnable runnable) {
		post(runnable, 0);
	}
	
	public void post(Runnable runnable, long delayedMilliSec) {
		post(runnable, delayedMilliSec, false);
	}
	
	public void post(Runnable runnable, long delayedMilliSec, boolean repeat) {
		post(new Message(-1, null, runnable, delayedMilliSec + System.currentTimeMillis(), repeat));
	}
	
	public void post(int code) {
		post(code, null);
	}
	
	public void post(int code, Object param) {
		post(code, param, 0);
	}
	
	public void post(int code, Object param, long delayedMilliSec) {
		post(code, param, delayedMilliSec, false);
	}
	
	public void post(int code, Object param, long delayedMilliSec, boolean repeat) {
		post(new Message(code, param, null, delayedMilliSec + System.currentTimeMillis(), repeat));
	}
	
	private void post(Message msg) {
		msgQueue.offer(msg);
	}

	public interface Dispatcher {
		/**
		 * This function will run in another thread.
		 * @param code indicator of the work to do
		 * @param param parameters for the work
		 */
		void dispatch(int code, Object param);
	}
	
	private class Message implements Delayed {
		final int code;
		final Object param;
		final Runnable runnable;
		long when; // millisecond
		final boolean repeat;
		final long gap;

		private Message(int code, Object param, Runnable runnable, long when, boolean repeat) {
			this.code = code;
			this.param = param;
			this.runnable = runnable;
			this.when = when;
			this.repeat = repeat;
			this.gap = repeat ? when - System.currentTimeMillis() : 0;
		}

		@Override
		public int compareTo(Delayed delayed) {
			Message msg = (Message) delayed;
	        return this.when > msg.when ? 1 : (this.when == msg.when ? 0 : -1);
		}

		@Override
		public long getDelay(TimeUnit timeUnit) {
			return timeUnit.convert(when - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
	}
	
	private class LooperThread extends Thread {
		@Override
		public void run() {
			try {
				for (;;) {
					// when queue is empty, wait until new message comes
					// when queue is not empty, wait until first msg's delay time comes
					// when waiting until first msg's delay time comes, and a new msg is offered
					// into the queue with less delay time, wait until the new msg's delay time comes
					Message msg = msgQueue.take();
					if (interrupted()) break;
					if (msg.runnable != null)
						msg.runnable.run();
					else
						dispatcher.dispatch(msg.code, msg.param);
					if (msg.repeat) {
						msg.when += msg.gap;
						msgQueue.offer(msg);
					}
					else {
						// TODO save this msg into message pool and reuse it in post(msg)
					}
				}
			} catch (InterruptedException e) {}
		}
	}
}

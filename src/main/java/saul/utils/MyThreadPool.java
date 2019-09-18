package saul.utils;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyThreadPool {
	private ArrayList<MyThread> threads;
	
	private ArrayBlockingQueue<Runnable> taskQueue;
	
	private int threadNumber;
	
	private int workThreadNumber;
	
	private final ReentrantLock mainLock = new ReentrantLock();
	
	public MyThreadPool(int threadNumber) {
		this.threadNumber = threadNumber;
		threads = new ArrayList<MyThread>(threadNumber);
		taskQueue = new ArrayBlockingQueue<Runnable>(threadNumber * 3);
		workThreadNumber = 0;
	}
	
	public void execute(Runnable thread) {
		try {
			mainLock.lock();
			
			//if the threadPool is not full, when a thread is added, start it
			if (workThreadNumber < threadNumber) {
				MyThread myThread = new MyThread(thread);
				myThread.start();
				threads.add(myThread);
				workThreadNumber++;
			}else {
				if (!taskQueue.offer(thread)) {
					log.warn("the task queue is full, can not add another more thread");
				}
			}
		}finally {
			mainLock.unlock();
		}
	}
	
	class MyThread extends Thread{
		private Runnable task;
		
		public MyThread(Runnable task) {
			this.task = task;
		}

		@Override
		public void run() {
			while(true) {
				if (task != null) {
					task.run();
					task = null;
				}else {
					Runnable queueTask = taskQueue.poll();
					if (queueTask != null) {
						queueTask.run();
					}
				}
			}
		}
		
	}

}


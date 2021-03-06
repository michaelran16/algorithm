package multi_threading.threadpool;

import java.util.ArrayList;
import java.util.List;

import multi_threading.blockingQ.implement.MyBlockingQueue;

public class ThreadPool {

	// a list of tasks (BlockingQueue)
	// a list of thread
	private MyBlockingQueue taskQueue = null;
	private List<MyThread> threads = new ArrayList<MyThread>();
	private boolean isStopped = false;

	public ThreadPool(int numThreads, int maxNumTasks) {
		taskQueue = new MyBlockingQueue(maxNumTasks);

		for (int i = 0; i < numThreads; i++) {
			threads.add(new MyThread(taskQueue));
		}
		System.out.println("Thread pool initiated. ");
		for (MyThread thread : threads) {
			thread.start();
		}
	}

	public synchronized void execute(Runnable task) {
		if (this.isStopped) {
			throw new IllegalStateException("ThreadPool is stopped");
		}

		try {
			this.taskQueue.enqueue(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean noMoreTask() {
		return this.taskQueue.isEmpty();
	}

	public synchronized void stop() {
		this.isStopped = true;
		for (MyThread thread : threads) {
			thread.stopMyThread();
			thread.interrupt();
		}
		System.out.println("Thread pool stopped. ");
	}
}

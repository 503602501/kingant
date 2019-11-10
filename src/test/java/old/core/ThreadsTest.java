package old.core;

public class ThreadsTest extends Thread{
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("阿斯顿发生地发");
		}
	}

}

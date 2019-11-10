
public class ThreadLocalExample {
	
	  public  class MyRunnable implements Runnable {

	        private ThreadLocal<Integer> threadLocal =
	               new ThreadLocal<Integer>();
	        private String s = null;
	        @Override
	        public void run() {
	            threadLocal.set( (int) (Math.random() * 100D) );
	            s =""+ threadLocal.get();

	            try {
	                Thread.sleep(2000);
	            } catch (InterruptedException e) {
	            }

	            System.out.println(Thread.currentThread().getName()+" "+ threadLocal.get()+" "+s);
	        }
	    }

	    public static void main(String[] args) throws InterruptedException {
	    	ThreadLocalExample e = new ThreadLocalExample();
	        MyRunnable sharedRunnableInstance = e.new MyRunnable();

	        Thread thread1 = new Thread(sharedRunnableInstance);
	        Thread thread2 = new Thread(sharedRunnableInstance);

	        thread1.start();
	        thread2.start();

	        thread1.join(); //wait for thread 1 to terminate
	        thread2.join(); //wait for thread 2 to terminate
	    }

}

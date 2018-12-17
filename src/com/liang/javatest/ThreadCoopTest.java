package com.liang.javatest;

public class ThreadCoopTest {

	private static volatile boolean flag = true;

	private static final Object lockOne = new Object();

	private static final Object lockTwo = new Object();

	static class ThreadOne extends Thread {

		@Override
		public void run() {

			for (int i = 1; i <= 100; i++) {
				synchronized (lockOne) {
					System.out.println(Thread.currentThread().getName() + "--" + i);
					lockOne.notify();
				}
				try {
					synchronized (lockTwo) {
						lockTwo.wait();
					}
				} catch (InterruptedException e) {
				}
			}
		}

	}

	static class ThreadTwo {
		Thread createNewThread() {
			return new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 1; i <= 100; i++) {
						try {
							synchronized (lockOne) {
								lockOne.wait();
								System.out.println(Thread.currentThread().getName() + "--" + i);
							}
							Thread.sleep(1000);
							synchronized (lockTwo) {
								lockTwo.notify();
							}
							
						} catch (InterruptedException e) {
						}

					}

				}
			});
		}
	}

	class ThreadThree{
		
		Thread createThreeThread() {
			return new Thread(new Runnable() {
				
				@Override
				public void run() {
					int i = 1;
					while(i <= 100) {
						if(flag) {
							System.out.println(Thread.currentThread().getName() + "--" + i);
							flag = false;
							i++;
						}
					}
				}
			});
		}
	}
	class ThreadFour {
		Thread createFourThread() {
			return new Thread(()-> {
				int i = 1;
				while(i <= 100) {
					if(!flag) {
						System.out.println(Thread.currentThread().getName() + "--" + i);
						flag = true;
						i++;
					}
				}
				
			});
		}
	}
	public ThreadThree geThreadThree() {
		return new ThreadThree();
	}
	
	public ThreadFour geThreadFour() {
		return new ThreadFour();
	}
	public static void main(String[] args) throws InterruptedException {
//		ThreadOne threadOne = new ThreadOne();
//		ThreadTwo threadTwo = new ThreadTwo();
//		
//		threadTwo.createNewThread().start();
//		Thread.sleep(1000);
//		threadOne.start();
		ThreadCoopTest ThreadCoopTest = new ThreadCoopTest();
		ThreadCoopTest.geThreadThree().createThreeThread().start();
		ThreadCoopTest.geThreadFour().createFourThread().start();
	}
}

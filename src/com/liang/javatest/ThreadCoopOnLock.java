package com.liang.javatest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class ThreadCoopOnLock {

	private final static ReentrantLock lockOne = new ReentrantLock();

	private final static ReentrantLock lockTwo = new ReentrantLock();

	private final static Condition lockConditionOne = lockOne.newCondition();

	private final static Condition lockConditionTwo = lockTwo.newCondition();

	private static volatile boolean flag = true;

	public static Consumer<Integer> print = (Integer item) -> {
		System.out.println(Thread.currentThread().getName() + "--" + item);
	};

	static class ThreadOne implements Runnable {

		private int i = 1;

		@Override
		public void run() {
			while (i <= 100) {
				try {
					lockTwo.lock();
					try {
						while (!flag) {
							lockConditionTwo.await();
						}
					} finally {
						lockTwo.unlock();
					}
					print.accept(i);
					lockOne.lock();
					try {
						//这句一定要在signal之前,否则有发生相互等待的可能,比如sinal调用发生在
						//线程2的await调用之前;
						flag = false;
						lockConditionOne.signal();
					} finally {
						lockOne.unlock();
					}
					i++;
				} catch (Exception e) {
				} finally {

				}
			}
		}

	}

	static class ThreadTwo implements Runnable {

		private int i = 1;

		@Override
		public void run() {
			try {
				while (i <= 100) {
					lockOne.lock();
					Thread.sleep(2000);
					try {
						while (flag) {
							lockConditionOne.await();
						}
					} finally {
						lockOne.unlock();
					}
					print.accept(i);
					lockTwo.lock();
					try {
						flag = true;
						lockConditionTwo.signal();
					} finally {
						lockTwo.unlock();
					}
					i++;
				}
			} catch (Exception e) {
			} finally {

			}
		}

	}

	public static void main(String[] args) throws InterruptedException {
		Thread threadOne = new Thread(new ThreadOne());
		Thread threadTwo = new Thread(new ThreadTwo());
		threadOne.start();
		threadTwo.start();

	}
}

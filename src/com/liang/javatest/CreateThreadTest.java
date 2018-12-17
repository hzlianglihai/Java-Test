package com.liang.javatest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class CreateThreadTest {
	
	private static  final Consumer<String> print = (String longInteger)->{
		System.out.println(longInteger);
	};
	static class ThreadClassTwo{
		
		Thread createNewThread() {
			return new Thread(new Runnable() {
				
				@Override
				public void run() {
					print.accept(Thread.currentThread().getName());
				}
			});
		}
		
	}
	
	static class ThreadCreateOne extends Thread{
		
		@Override
		public void run() {
			print.accept(Thread.currentThread().getName());
		}
	}
	
	static class ThreadCreateThree implements Runnable {

		@Override
		public void run() {
			print.accept(Thread.currentThread().getName());			
		}
		
	}
	static class CallTask implements Callable<Map<String, Object>>{

		@Override
		public Map<String, Object> call() throws Exception {
			HashMap<String, Object> result = new HashMap<>();
			System.out.println(Thread.currentThread().getName());
			result.put("calltask", Thread.currentThread().getName());
			return result;
		}
		
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ThreadCreateOne threadCreateOne = new ThreadCreateOne();
		threadCreateOne.start();
		
		ThreadClassTwo threadClassTwo = new ThreadClassTwo();
		threadClassTwo.createNewThread().start();
		
		ThreadCreateThree threadCreateThree = new ThreadCreateThree();
		new Thread(threadCreateThree).start();
		
		FutureTask<Map<String,Object>> futureTask = new FutureTask<>(new CallTask());
		new Thread(futureTask).start();
		Map<String,Object> result = futureTask.get();
		System.out.println(result);
		
	}
}

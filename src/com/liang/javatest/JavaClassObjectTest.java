package com.liang.javatest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

class ClassTest1{
	
}
class ClassTest2{
	
}
public class JavaClassObjectTest extends ClassTest1{
	private static int a = 0;
	 static class Toy extends ClassTest2{
		Toy() {
			System.out.println("Toy constructor" + a);
		}

		Toy(int i) {
			System.out.println("Toy constructor : " + i);
		}

	}

	  class FancyToy extends Toy {
		FancyToy() {
			super(1);
		}
		void outer() {
			System.out.println(JavaClassObjectTest.this);
		}
		public void invoke(Integer param1,String param2) {
			System.out.println("just for test : " + param1 + param2);
		}
	}
	static void printinfo(Class cc) {
		System.out.println("Class name : " + cc.getName() + " is Interface : " + cc.isInterface());
	}
	private  FancyToy getFancyToy() {
		return new FancyToy();
	}

	public static void reflect() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class<?> c = null;
		List<String> list = new ArrayList<>();
		list.toArray();
		String [] test = {};
		Arrays.asList(test);
		List<String> stooges = Arrays.asList("Larry", "Moe", "Curly");
		stooges.set(0, "hgshsh");
		System.out.println(stooges.toString());
//		stooges.add(0, "shfsfj");
//		System.out.print(stooges.toString());
		JavaClassObjectTest javaClassObjectTest = new JavaClassObjectTest();
		JavaClassObjectTest.FancyToy fancyToy = null;
		for(int i = 0; i < 10; i++) {
			fancyToy = javaClassObjectTest.getFancyToy();
			System.out.println(fancyToy);
			fancyToy.outer();
		}
//		FancyToy fancyToy = getFancyToy();
		try {
//			c = Class.forName("FancyToy");
			c = FancyToy.class;
			Class up = c.getSuperclass();
			//??
			Class down = c.getClass();
			printinfo(c);
			//??
			printinfo(down);
			printinfo(up);
			printinfo(FancyToy.class);
			Method[] methods = c.getMethods();
			String methodName = "invoke";
			for(Method method : methods) {
				System.out.println(method.getName() + " : " + method.getParameterTypes());
				if(method.getParameterTypes() != null) {
					for(int i = 0; i < method.getParameterTypes().length;i++) {
						System.out.println(method.getParameterTypes()[i].getName());
					}
				}
			}
			Object [] args = {4,"enter"}; 
			Class<?> [] parameterTypes = new Class<?>[args.length];
//			for(int i = 0; i < args.length; i++) {
				parameterTypes[0] = Integer.class;
				parameterTypes[1] = String.class;
//			}
			Method method = c.getMethod(methodName, parameterTypes );
			method.invoke(fancyToy, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws InterruptedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		int i = 1;
		while(i != 0) {
			JavaClassObjectTest javaClassObjectTest = new JavaClassObjectTest();
			javaClassObjectTest.reflect();
			System.out.println(i--);
			Thread.sleep(2000);
		}
	}
}

package com.liang.javatest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Toy {
	Toy() {
		System.out.println("Toy constructor");
	}

	Toy(int i) {
		System.out.println("Toy constructor : " + i);
	}

}

class FancyToy extends Toy {
	FancyToy() {
		super(1);
	}
}
public class JavaClassObjectTest {

	static void printinfo(Class cc) {
		System.out.println("Class name : " + cc.getName() + " is Interface : " + cc.isInterface());
	}

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class c = null;
		try {
			c = Class.forName("com.liang.javatest.FancyToy");
			Class up = c.getSuperclass();
			//??
			Class down = c.getClass();
			printinfo(c);
			//??
			printinfo(down);
			printinfo(up);
			printinfo(FancyToy.class);
			Method[] methods = FancyToy.class.getMethods();
			for(Method method : methods) {
				System.out.println(method.getName() + method.getParameterTypes());
				//??
//				method.invoke(c.newInstance(), args);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

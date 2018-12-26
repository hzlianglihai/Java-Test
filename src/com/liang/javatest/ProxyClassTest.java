package com.liang.javatest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyClassTest {
	class SimpleProxyClass implements InvocationHandler {

		private Object target;

		SimpleProxyClass(Object target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println("proxy : " + proxy.getClass() + " methodName : " + method.getName() + " args : " + args);
			Thread.sleep(1000);
			if (args != null) {
				for (Object arg : args) {
					System.out.println(arg.toString());
				}
			}
			Thread.sleep(2000);
			return method.invoke(proxy, args);
		}

	}

	interface Target {

		void methodInvokeTest(String obj);
	}

	class TargetImpl implements Target {

		public void methodInvokeTest(String obj) {
			System.out.println("just for test : " + obj);
		}
	}

	public SimpleProxyClass getProxyClass(Target target) {
		return new SimpleProxyClass(target);
	}

	public Target getTarget() {
		return new ProxyClassTest.TargetImpl();
	}

	public static void main(String[] args) {
		ProxyClassTest proxyClassTest = new ProxyClassTest();

		Target target = proxyClassTest.getTarget();
		
//		Target target1 = new ProxyClassTest.Target();

		Target targetProxy = (Target) Proxy.newProxyInstance(Target.class.getClassLoader(),
				new Class[] { Target.class }, proxyClassTest.getProxyClass(target));
		targetProxy.methodInvokeTest("hello");
	}

}

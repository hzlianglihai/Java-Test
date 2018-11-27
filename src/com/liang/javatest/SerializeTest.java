package com.liang.javatest;

import java.io.Serializable;

public class SerializeTest {
	 class Person implements Serializable {
		private static final long serialVersionUID = -5756630803888689062L;
		public int a;
		    public String b;
		}
		Person a = new Person();
		a.a = 1;
		a.b = "上海";
		long now = System.currentTimeMillis(); for (int i = 0; i < 1000000; i++) {
		    byte data[] = serializer(a);
		}
		long cost1 = System.currentTimeMillis() - now;
		System.out.println("jdk serialize cost " + cost1 + "ms");
		System.out.println("-------------------------------------------------
		--------");
		now = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
		    byte data[] = ProtoStuffUtils.serializer(a);
		}
		long cost2 = System.currentTimeMillis() - now;
		System.out.println("protobuf serialize cost " + cost2 + "ms");
}

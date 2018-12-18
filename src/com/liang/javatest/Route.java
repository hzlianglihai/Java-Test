package com.liang.javatest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * TODO
 *
 * @author hzlianglihai<hzlianglihai@corp.netease.com>
 * @since 2018年12月17日
 */
public class Route {

	private static HashMap<Integer, Integer> map = new HashMap<>();
	
	private final Random random = new Random();
	
	 static {
		 map.put(1, 100);
		 map.put(2, 20);
	 }
	
	public Integer select(Object url) {
		int totalWeight = 0;
		int off = 0;
		for(int i = 0; i < 2; i++) {
			totalWeight += map.get(i);
		}
		for(int i = 0; i < 2; i++) {
			off = random.nextInt(totalWeight);
			if(off < 0) {
				return i;
			}
		}
		return null;
	}
}

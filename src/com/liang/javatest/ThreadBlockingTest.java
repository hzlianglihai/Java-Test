package com.liang.javatest;

import java.util.ArrayList;
import java.util.List;

public class ThreadBlockingTest {

	
	List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);
    List<Integer> numsWithoutNull = nums.stream().filter(num -> num != null).
            collect(() -> new ArrayList<Integer>(),
                    (list, item) -> list.add(item),
                    (list1, list2) -> list1.addAll(list2));
}

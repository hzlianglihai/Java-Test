package com.liang.javatest;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ArraysTest {
	
	
	interface SortArrays<T>{
		
		List<T> sortArrays(List<T> list,Comparator<? super T> c);
	}
	
	class MergeSortClass<T> implements SortArrays<T> {
		
		private final List<T> LIST = new ArrayList<>();

		@Override
		public List<T> sortArrays(List<T> list,Comparator<? super T> c) {
			LIST.addAll(list);
			return mergeSortList(list,0,list.size() - 1,c);
		}

		private List<T> mergeSortList(List<T> list, int start, int end,Comparator<? super T> c) {
			
			if(start == end) {
				return list;
			}
			int m = (end + start) / 2;
			List<T> listLeft = mergeSortList(LIST.subList(start, m),start,m,c);
			List<T> listRight = mergeSortList(LIST.subList(m + 1, end),m + 1,end,c);
			return mergeList(listLeft, listRight,c);
		}

		private List<T> mergeList(List<T> listLeft, List<T> listRight,Comparator<? super T> c) {
			List<T> list = new ArrayList<>();
			int i = 0,j = 0;
			for(i = 0,j = 0; i < listLeft.size() && j < listRight.size(); ) {
				if(c.compare(listLeft.get(i), listRight.get(j)) < 0) {
					list.add(listLeft.get(i));
					i++;
				}else {
					list.add(listRight.get(j));
					j++;
				}
			}
			if(i < listLeft.size()) {
				list.addAll(listLeft.subList(i, listLeft.size()));
			}
			if(j < listRight.size()) {
				list.addAll(listRight.subList(j, listRight.size()));
				
			}
			return list;
			
		}
		
	}
	<T> SortArrays<T> getSortArrays(){
		return new MergeSortClass<T>();
	}
	public static void main(String[] args) {
		ArraysTest arraysTest = new ArraysTest();
		Integer [] arrayOne = new Integer[10];
		Random random = new Random();
		for(int i = 0; i < arrayOne.length; i++) {
			arrayOne[i] = random.nextInt(10000);
		}
		List<Integer> list = Arrays.asList(arrayOne);
		Iterator<Integer> iterators = (Iterator<Integer>) list.subList(0, 2).iterator();
		Iterator<Integer> iterators2 = (Iterator<Integer>) list.iterator();
//		while(iterators.hasNext()) {
//			System.out.println(iterators.next());
//		}
//		
		while(iterators2.hasNext()) {
			System.out.println(iterators2.next());
		}
		SortArrays<Integer> sortArrays = arraysTest.getSortArrays();
		List<Integer> listSorted = sortArrays.sortArrays(list, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 < o2 ) {
					return -1;
				}else if(o1 == o2) {
					return 0;
				}
				return 1;
			}
		});
		System.out.println("sorted");
		Iterator<Integer> iterators3 = (Iterator<Integer>) listSorted.iterator();
		while(iterators3.hasNext()) {
			System.out.println(iterators3.next());
		}
	}

}

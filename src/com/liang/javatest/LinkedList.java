package com.liang.javatest;

/**
 * TODO
 *
 * @author hzlianglihai<hzlianglihai@corp.netease.com>
 * @since 2018年12月17日
 */
public class LinkedList<E> {
	
	private Node<E> last;
	private Node<E> first;
	
	private static class Node<E> {
		Node<E> prev;
		Node<E> next;
		E item;
		
		Node (Node<E> prev,E element,Node<E> next){
			this.prev = prev;
			this.item = element;
			this.next = next;
		}
	}
	
	public boolean add(E e) {
		return addLast(e);
	}
	
	private boolean addLast(E e) {
		Node<E> l = last;
		Node<E> newNode = new Node<E>(l,e,null);
		if(l == null) {
			newNode = last;
		}else {
			l.next = newNode;
		}
		return true;
	}

	public boolean remove(E e) {
		for (Node<E> x = first; x != null; x = x.next) {
            if (e.equals(x.item)) {
                removeItem(x);
                return true;
            }
        }
		return true;
	}

	private void removeItem(Node<E> x) {
		Node<E> prev = x.prev;
		Node<E> next = x.next;
		E item = x.item;
		//头结点
		if(prev == null) {
			first = next;
		}else {
			prev.next = next;
		}
		//尾节点
		if(next == null) {
			prev.next = null;
			last = prev;
		}else {
			next.prev = prev;
		}
	}

}

package oripa.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import oripa.util.LazyList;

public class LazyArrayList<E> implements LazyList<E>{

	private ArrayList<E> list;
	
	public LazyArrayList() {
		list = new ArrayList<>();
	}
	
	public LazyArrayList(int capacity){
		list = new ArrayList<>(capacity);
	}
	
	public LazyArrayList(Collection<? extends E> c){
		list = new ArrayList<>(c);
	}
	
	public boolean add(E e) {
		return list.add(e);
	}

	public void add(int index, E element) {
		list.add(index, element);
	}

	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return list.addAll(index, c);
	}

	public void clear() {
		list.clear();
	}

	public Object clone() {
		return list.clone();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public void ensureCapacity(int minCapacity) {
		list.ensureCapacity(minCapacity);
	}

	public boolean equals(Object o) {
		return list.equals(o);
	}

	public E get(int index) {
		return list.get(index);
	}

	public int hashCode() {
		return list.hashCode();
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Iterator<E> iterator() {
		return list.iterator();
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	public E remove(int index) {
		return list.remove(index);
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	public E set(int index, E element) {
		return list.set(index, element);
	}

	public int size() {
		return list.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	public String toString() {
		return list.toString();
	}

	public void trimToSize() {
		list.trimToSize();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9179072503777118595L;

	/**
	 * bring the tail of this list to the index place.
	 * @param index
	 * @return next index
	 */
	@Override
	public int lazyRemove(int index){
		int tail = list.size() - 1;
		list.set(index, list.get(tail));
		
		// skip shifting latter items
		
		list.remove(tail);

		return index;
	}

	
	
	
//	@Override
//	public boolean add(E e) {
//		if(tail < this.size() - 1){
//			this.set(tail, e);
//		}
//		else {
//			super.add(e);
//		}
//		
//		tail++;
//		return true;
//	}
//
//	@Override
//	public void add(int index, E element) {
//		super.add(index, element);
//		tail++;
//	}
//	
	
}

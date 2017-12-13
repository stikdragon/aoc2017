package uk.co.stikman.aoc.utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SortableSet<T> implements Iterable<T> {
	private final List<T>	list;
	private final Set<T>	lkp;
	private Comparator<T>	comp;
	private boolean			dirty;

	public SortableSet(Comparator<T> comp) {
		this.comp = comp;
		this.list = new LinkedList<T>();
		this.lkp = new HashSet<T>();
	}

	public void add(T x) {
		if (!lkp.add(x)) // already here
			return;
		list.add(x);
		dirty = true;
	}

	private final void ensureSorted() {
		if (dirty) {
			list.sort(comp);
			dirty = false;
		}
	}

	public T get(int idx) {
		ensureSorted();
		return list.get(idx);
	}

	public T removeFirst() {
		ensureSorted();
		T x = list.remove(0);
		lkp.remove(x);
		return x;
	}

	public boolean contains(T x) {
		return lkp.contains(x);
	}

	public Iterator<T> iterator() {
		ensureSorted();
		return list.iterator();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public String toString() {
		ensureSorted();
		return list.toString();
	}

}

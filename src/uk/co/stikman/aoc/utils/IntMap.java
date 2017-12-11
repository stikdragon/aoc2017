package uk.co.stikman.aoc.utils;

import java.util.Collection;
import java.util.HashMap;

public class IntMap<T> {
	private final HashMap<T, Integer> map = new HashMap<>(); 
	private final int defaultValue;
	
	public IntMap(int defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public int get(T x) {
		Integer r = map.get(x);
		if (r == null)
			return defaultValue;
		return r.intValue();
	}

	public int put(T x, int val) {
		Integer o = map.put(x, val);
		if (o == null)
			return 0;
		return o.intValue();
	}
	
	public Collection<T> coordList() {
		return map.keySet();
	}

	public int size() {
		return map.size();
	}
}
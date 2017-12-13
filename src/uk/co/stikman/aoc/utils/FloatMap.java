package uk.co.stikman.aoc.utils;

import java.util.Collection;
import java.util.HashMap;

public class FloatMap<T> {
	private final HashMap<T, Float> map = new HashMap<>(); 
	private final float defaultValue;
	
	public FloatMap(float defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public float get(T x) {
		Float r = map.get(x);
		if (r == null)
			return defaultValue;
		return r.floatValue();
	}

	public float put(T x, float val) {
		Float o = map.put(x, val);
		if (o == null)
			return 0;
		return o.floatValue();
	}
	
	public Collection<T> coordList() {
		return map.keySet();
	}

	public int size() {
		return map.size();
	}
}
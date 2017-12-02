package uk.co.stikman.aoc.utils;

public class Util {

	public static final int clamp(int val, int low, int high) {
		if (val < low)
			return low;
		if (val > high)
			return high;
		return val;
	}

}

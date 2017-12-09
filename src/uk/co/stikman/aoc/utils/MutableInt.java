package uk.co.stikman.aoc.utils;

public class MutableInt {
	private int n;
	
	public void inc(int a) {
		n+=a;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
}

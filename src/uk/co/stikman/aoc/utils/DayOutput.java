package uk.co.stikman.aoc.utils;

public class DayOutput implements Output {

	private int day;

	public DayOutput(int day) {
		this.day = day;
	}

	@Override
	public void println(String s) {
		for (String x : s.split("\n"))
			System.out.println("[Day " + day + "] " + x);
	}

}

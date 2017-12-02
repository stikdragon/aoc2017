package uk.co.stikman.aoc2017.utils;

public class ConsoleOutput implements Output {

	@Override
	public void println(String s) {
		System.out.println(s);
	}

}

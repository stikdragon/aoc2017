package uk.co.stikman.aoc2017;

import uk.co.stikman.aoc2017.utils.Output;

public class RunAll {

	public static void main(String[] args) {
		run(Day1.class, 1);
		run(Day2.class, 2);
	}

	private static void run(Class<? extends AoCBase> cls, int day) {
		AoCBase a;
		try {
			a = cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("Day " + day + " failed");
			e.printStackTrace();
			return;
		}
		Output output = new Output() {
			@Override
			public void println(String s) {
				System.out.println("[Day " + day + "] " + s);
			}
		};
		a.run(SourceData.get(day), 0, output);
		a.run(SourceData.get(day), 1, output);
	}

}

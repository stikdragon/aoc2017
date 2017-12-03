package uk.co.stikman.aoc.year2017;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.DayOutput;
import uk.co.stikman.aoc.utils.Output;

public class RunAll {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		for (int i = 1; i <= 25; ++i) {
			Class<? extends AoCBase> cls;
			try {
				cls = (Class<? extends AoCBase>) Class.forName("uk.co.stikman.aoc.year2017.Day" + i);
				run(cls, i);
			} catch (ClassNotFoundException e) {
			} catch (Throwable th) {
				System.err.println("Error on day " + i);
				th.printStackTrace();
			}
		}
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
		Output output = new DayOutput(day);
		a.run(SourceData.get(day), 0, output);
		a.run(SourceData.get(day), 1, output);
	}

}

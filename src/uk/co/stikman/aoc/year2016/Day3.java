package uk.co.stikman.aoc.year2016;

import java.util.Arrays;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day3 extends AoCBase {
	public static void main(String[] args) {
		new Day3().run(SourceData.get(3), 0, new ConsoleOutput());
		new Day3().run(SourceData.get(3), 1, new ConsoleOutput());
	}

	//@formatter:off
	private static final String PART2PAD = 
			   "--1--"
			 + "-234-"
             + "56789"
			 + "-ABC-"
             + "--D--";
	//@formatter:on

	@Override
	public void run(String input, int part, Output out) {
		if (part == 0) {
			int count = 0;
			for (String line : SourceData.get(3).split("\n")) {
				line = line.trim();
				int[] sides = Arrays.stream(line.split(" +")).mapToInt(Integer::parseInt).toArray();
				if (testTriangle(sides))
					++count;
			}
			out.println("Valid triangles: " + count);
		} else if (part == 1) {

			//
			// do in groups of three
			//
			int count = 0;
			int idx = 0;
			String[] lines = new String[3];
			for (String line : SourceData.get(3).split("\n")) {
				line = line.trim();
				lines[idx] = line;
				++idx;
				if (idx == 3) {
					//
					// a group
					//
					if (testGroup(lines, 0))
						++count;
					if (testGroup(lines, 1))
						++count;
					if (testGroup(lines, 2))
						++count;

					idx = 0;
				}
			}
			out.println("Valid triangles: " + count);

		} else
			throw new RuntimeException("Part.. ?");

	}

	private boolean testGroup(String[] lines, int col) {
		int[] sides = new int[3];
		for (int i = 0; i < 3; ++i)
			sides[i] = Integer.parseInt(lines[i].split(" +")[col]);
		return testTriangle(sides);
	}

	private boolean testTriangle(int[] sides) {
		if (sides[0] + sides[1] <= sides[2])
			return false;
		if (sides[1] + sides[2] <= sides[0])
			return false;
		if (sides[0] + sides[2] <= sides[1])
			return false;
		return true;
	}

}

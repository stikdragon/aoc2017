package uk.co.stikman.aoc.year2017;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.IntList;
import uk.co.stikman.aoc.utils.Output;

public class Day2 extends AoCBase {

	public static void main(String[] args) {
		new Day2().run(SourceData.get(2), 0, new ConsoleOutput());
		new Day2().run(SourceData.get(2), 1, new ConsoleOutput());
	}

	/**
	 * <p>
	 * Part 1: The spreadsheet consists of rows of apparently-random numbers. To
	 * make sure the recovery process is on the right track, they need you to
	 * calculate the spreadsheet's checksum. For each row, determine the
	 * difference between the largest value and the smallest value; the checksum
	 * is the sum of all of these differences.
	 * 
	 * <p>
	 * Part 2: It sounds like the goal is to find the only two numbers in each
	 * row where one evenly divides the other - that is, where the result of the
	 * division operation is a whole number. They would like you to find those
	 * numbers on each line, divide them, and add up each line's result.
	 * 
	 */
	@Override
	public void run(String input, int part, Output output) {
		List<String> lines = Arrays.asList(input.split("\n"));
		List<IntList> rows = lines.stream().map(s -> IntList.parseList(s, "\t")).collect(Collectors.toList());
		if (part == 0) {
			List<Integer> min = rows.stream().map(lst -> lst.stream().min().orElse(0)).collect(Collectors.toList());
			List<Integer> max = rows.stream().map(lst -> lst.stream().max().orElse(0)).collect(Collectors.toList());
			if (min.size() != max.size())
				throw new RuntimeException("Expected same number of min and max items");
			int sum = 0;
			for (int i = 0; i < min.size(); ++i)
				sum += max.get(i) - min.get(i);
			output.println("Checksum for part 0 = " + sum);
		} else if (part == 1) {
			int sum = 0;
			for (IntList row : rows) {
				//
				// try every number with every other number
				//
				Set<Integer> pair = new HashSet<>();
				for (int i = 0; i < row.size(); ++i) {
					for (int j = 0; j < i; ++j) {
						int a = row.get(i);
						int b = row.get(j);
						if (a > b) {
							int t = a;
							a = b;
							b = t;
						}
						if (b % a == 0) {
							pair.add(a);
							pair.add(b);
						}
					}
				}
				if (pair.size() != 2)
					throw new RuntimeException("Found a row that doesn't have exactly 2 divisible ints: " + row);
				output.println("Row pairs: " + pair);
				Iterator<Integer> iter = pair.iterator();
				int a = iter.next();
				int b = iter.next();
				if (a > b) { // hashset won't be in order they were added, so min/max
					int t = a;
					a = b;
					b = t;
				}
				sum += (b / a);
			}
			output.println("Checksum for part 1 = " + sum);
		}
	}

}

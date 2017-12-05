package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day5 extends AoCBase {

	public static void main(String[] args) {
		new Day5().run(SourceData.get(5), 0, new ConsoleOutput());
		new Day5().run(SourceData.get(5), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output output) {
		List<Integer> jumps = Arrays.stream(input.split("\n")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
		int cnt = 0;
		int ptr = 0;
		while (ptr >= 0 && ptr < jumps.size()) {
			int j = jumps.get(ptr);
			if (part == 0)
				jumps.set(ptr, j + 1);
			else if (part == 1)
				jumps.set(ptr, j >= 3 ? j - 1 : j + 1);
			ptr += j;
			++cnt;
		}
		output.println("Part " + (part + 1) + " takes " + cnt + " jumps to escape");
	}

}

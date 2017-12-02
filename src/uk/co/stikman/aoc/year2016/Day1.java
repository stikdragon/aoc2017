package uk.co.stikman.aoc.year2016;

import java.util.HashSet;
import java.util.Set;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.Pair;

public class Day1 extends AoCBase {
	public static void main(String[] args) {
		new Day1().run(SourceData.get(1), 0, new ConsoleOutput());
		new Day1().run(SourceData.get(1), 1, new ConsoleOutput());
	}

	Set<Pair<Integer, Integer>> visited = new HashSet<>();

	@Override
	public void run(String input, int part, Output out) {
		visited.clear();

		String[] instructions = SourceData.get(1).split(",");
		int x = 0;
		int y = 0;
		int dir = 0; // north
		visited.add(new Pair<Integer, Integer>(x, y));
		int counter = 0;
		for (String inst : instructions) {
			++counter;
			inst = inst.trim();
			switch (inst.charAt(0)) {
				case 'L':
				case 'l':
					--dir;
					break;
				case 'R':
				case 'r':
					++dir;
					break;
				default:
					throw new RuntimeException("Invalid input: " + inst);
			}
			while (dir < 0)
				dir += 4;
			while (dir > 3)
				dir -= 4;

			int amt = Integer.parseInt(inst.substring(1));
			switch (dir) {
				case 0:
					while (amt-- > 0) {
						++y;
						if (part == 1 && checkVisited(x, y, out))
							return;
					}
					break;
				case 1:
					while (amt-- > 0) {
						++x;
						if (part == 1 && checkVisited(x, y, out))
							return;
					}
					break;
				case 2:
					while (amt-- > 0) {
						--y;
						if (part == 1 && checkVisited(x, y, out))
							return;
					}
					break;
				case 3:
					while (amt-- > 0) {
						--x;
						if (part == 1 && checkVisited(x, y, out))
							return;
					}
					break;
			}

			out.println(counter + ": " + x + ", " + y);

		}

		if (part == 1)
			throw new RuntimeException("Couldn't find an intersection");
		//
		// "taxicab" distance to x,y:
		//
		out.println("Distance: " + (x + y));
	}

	private boolean checkVisited(int x, int y, Output out) {
		boolean b = !visited.add(new Pair<>(x, y));
		if (b)
			out.println("Revisited a block. Distance = " + (x + y));
		return b;
	}

}

package uk.co.stikman.aoc.year2017;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Grid;
import uk.co.stikman.aoc.utils.Output;

public class Day3 extends AoCBase {

	public static void main(String[] args) {
		new Day3().run(SourceData.get(3), 0, new ConsoleOutput());
		new Day3().run(SourceData.get(3), 1, new ConsoleOutput());
	}

	/**
	 * Grid:
	 * 
	 * <pre>
	 * 
		17  16  15  14  13
		18   5   4   3  12
		19   6   1   2  11
		20   7   8   9  10
		21  22  23---> ...
	 * </pre>
	 */
	@Override
	public void run(String input, int part, Output output) {
		long address = Long.parseLong(input);
//		address = 20;
		//
		// just brute force it, hopefully the numbers aren't too big (mine isn't)
		//
		Grid<Long> grid = new Grid<>();
		int x = 0;
		int y = 0;
		int dir = 0;
		int sidelen = 1;
		long idx = 1;
		grid.put(0, 0, Long.valueOf(1)); // seed the centre cell
		outer: for (;;) {
			++sidelen;
			for (int i = 0; i < sidelen / 2; ++i) {
				if (part == 1) {
					//
					// calculate sum 
					//
					long sum = 0;
					for (int dx = -1; dx < 2; ++dx) {
						for (int dy = -1; dy < 2; ++dy) {
							Long n = grid.get(x + dx, y + dy);
							if (n != null)
								sum += n;
						}
					}
					if (sum > address) {
						output.println(grid.render(8));
						output.println("Part 2: Position " + x + ", " + y + " is greater than puzzle input: " + sum);
						return;
					}
					grid.put(x, y, sum);
				}
				if (idx == address)
					break outer;
				switch (dir) {
					case 0:
						++x;
						break;
					case 1:
						++y;
						break;
					case 2:
						--x;
						break;
					case 3:
						--y;
						break;
				}
				++idx;

			}
			++dir;
			if (dir > 3)
				dir = 0;
		}
		output.println("Position " + address + " is at: " + x + ", " + y + " which is " + (Math.abs(x) + Math.abs(y)) + " away from the centre");
	}

}

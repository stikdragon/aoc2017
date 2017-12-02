package uk.co.stikman.aoc.year2016;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.Util;

public class Day2 extends AoCBase {
	public static void main(String[] args) {
		new Day2().run(SourceData.get(2), 0, new ConsoleOutput());
		new Day2().run(SourceData.get(2), 1, new ConsoleOutput());
	}

	private int					curX;
	private int					curY;

	/**
	 * keypad is for part 1:
	 * 
	 * <pre>
	 * 123
	 * 456
	 * 789
	 * </pre>
	 * 
	 * and
	 * 
	 * <pre>
	 *     
			1
		  2 3 4
		5 6 7 8 9
		  A B C
		    D
	 * </pre>
	 * 
	 * for part 2
	 */

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
			curX = 1;
			curY = 1;
			String[] lines = input.split("\n");
			String code = "";
			for (String line : lines)
				code += evalDigitPart1(line);
			out.println("Code is: " + code);

		} else if (part == 1) {
			curX = 0; // start on the "5"
			curY = 2;
			String[] lines = input.split("\n");
			String code = "";
			for (String line : lines)
				code += evalDigitPart2(line);
			out.println("Code is: " + code);

		} else
			throw new RuntimeException("Part ???");
	}

	private char evalDigitPart2(String line) {
		for (char ch : line.toCharArray()) {
			int dx = 0;
			int dy = 0;
			switch (ch) {
				case 'R':
					dx = 1;
					break;
				case 'L':
					dx = -1;
					break;
				case 'U':
					dy = -1;
					break;
				case 'D':
					dy = 1;
					break;
			}

			if (getKeyAt(curX + dx, curY + dy) != '-') {
				curX += dx;
				curY += dy;
			}
		}
		return getKeyAt(curX, curY);
	}

	/**
	 * Checks for out of bounds and returns -
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private char getKeyAt(int x, int y) {
		if (x < 0 || x > 4 || y < 0 || y > 4)
			return '-';
		return PART2PAD.charAt(y * 5 + x);
	}

	private char evalDigitPart1(String line) {
		for (char ch : line.toCharArray()) {
			switch (ch) {
				case 'R':
					++curX;
					break;
				case 'L':
					--curX;
					break;
				case 'U':
					--curY;
					break;
				case 'D':
					++curY;
					break;
			}
			curX = Util.clamp(curX, 0, 2);
			curY = Util.clamp(curY, 0, 2);
		}

		return (char) ('1' + (curY * 3 + curX));
	}

}

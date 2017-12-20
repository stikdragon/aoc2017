package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.TextPainter;
import uk.co.stikman.aoc.utils.Util;

public class Day19 extends AoCBase {

	public static void main(String[] args) {
		new Day19().run(SourceData.get(19), 0, new ConsoleOutput());
		new Day19().run(SourceData.get(19), 1, new ConsoleOutput());
	}

	public static class World {
		private int[]	data;
		private int		height;
		private int		width;
		private int		x;
		private int		y;
		private int		dir	= 0;	// 0123 = SENW

		public World(int width, int height) {
			data = new int[width * height];
			this.width = width;
			this.height = height;
		}

		public void setLine(int y, char[] chars) {
			int off = y * width;
			for (int x = 0; x < width; ++x) {
				if (x >= chars.length)
					return;
				char ch = chars[x];
				int r = -1;
				if (ch == '-' || ch == '+' || ch == '|')
					r = 0;
				else if (ch >= 'A' && ch <= 'Z')
					r = ch - 'A' + 1;
				data[x + off] = r;
			}
		}

		public void findStart() {
			y = 0;
			x = -1;
			dir = 0;
			for (int x = 0; x < width; ++x) {
				if (data[x] != -1) {
					this.x = x;
					break;
				}
			}
			if (x == -1)
				throw new RuntimeException("Couldn't find the start");
		}

		@Override
		public String toString() {
			TextPainter tp = new TextPainter();

			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					int n = get(x, y);
					if (n == 0)
						tp.putChar(x, y, '#');
					else if (n > 0)
						tp.putChar(x, y, (char) ('A' + (n - 1)));
					else 
						tp.putChar(x, y, ' ');
				}
			}
			return tp.toString();
		}

		/**
		 * returns -1 if nothing, 0 if path, 1..N if letter. Out of range
		 * returns -1
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		public int get(int x, int y) {
			if (x < 0 || y < 0)
				return -1;
			if (x >= width || y >= height)
				return -1;
			return data[y * width + x];
		}

		public int run(Consumer<String> out) {

			int[] lkp = new int[64];
			for (int i = 0; i < lkp.length; ++i)
				lkp[i] = -1; // invalid config

			lkp[Integer.parseInt("001100", 2)] = 3;
			lkp[Integer.parseInt("000110", 2)] = 1;
			lkp[Integer.parseInt("000100", 2)] = -2;

			lkp[Integer.parseInt("011001", 2)] = 0;
			lkp[Integer.parseInt("011100", 2)] = 2;
			lkp[Integer.parseInt("011000", 2)] = -2;

			lkp[Integer.parseInt("100011", 2)] = 1;
			lkp[Integer.parseInt("101001", 2)] = 3;
			lkp[Integer.parseInt("100001", 2)] = -2;

			lkp[Integer.parseInt("110110", 2)] = 2;
			lkp[Integer.parseInt("110011", 2)] = 0;
			lkp[Integer.parseInt("110010", 2)] = -2;

			int steps = 1;
			for (;;) {
				++steps;
				int dx = 0;
				int dy = 0;
				switch (dir) {
					case 0:
						dy = 1;
						break;
					case 1:
						dx = 1;
						break;
					case 2:
						dy = -1;
						break;
					case 3:
						dx = -1;
						break;
				}

				int r = get(x + dx, y + dy);
				if (r != -1) {
					if (r > 0)
						out.accept("Passed " + (char) (r + 64) + " at " + x + "," + y);

					x += dx;
					y += dy;
					continue; // nothing to do here
				}

				//
				// need to find a new direction
				//
				int n = 0;
				n |= get(x, y + 1) == -1 ? 0 : 1;
				n |= get(x + 1, y) == -1 ? 0 : 2;
				n |= get(x, y - 1) == -1 ? 0 : 4;
				n |= get(x - 1, y) == -1 ? 0 : 8;
				n |= dir << 4;
				dir = lkp[n];
				--steps; // on a direction turn we'd end up double counting these

				//				out.accept("Changing direction at " + x + ", " + y + " to " + dir);

				if (dir == -2) {
					out.accept("Finished at " + x + ", " + y);
					return steps;
				}
				if (dir == -1)
					throw new RuntimeException("Invalid config at " + x + ", " + y);
			}

		}

	}

	@Override
	public void run(String input, int part, Output out) {
		String[] lines = input.split("\n");
		int w = Arrays.stream(lines).mapToInt(x -> x.length()).max().orElse(0);
		World world = new World(w, lines.length);
		int y = 0;
		for (String line : input.split("\n"))
			world.setLine(y++, line.toCharArray());
		world.findStart();
		if (part == 0) {
			world.run(x -> out.println(x));
		} else if (part == 1) {
			out.println("That's " + world.run(x -> {
			}) + " steps");
		}
	}

}

package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.TextPainter;
import uk.co.stikman.aoc.utils.Util;

public class Day15 extends AoCBase {

	public static void main(String[] args) {
		new Day15().run(SourceData.get(15), 0, new ConsoleOutput());
		new Day15().run(SourceData.get(15), 1, new ConsoleOutput());
	}

	private static class Generator {
		private static final long	MAGIC	= 2147483647L;
		private final int			factor;
		private long				previous;
		private final int			modfactor;

		public Generator(int factor, long startingval, int modfactor) {
			super();
			this.factor = factor;
			this.previous = startingval;
			this.modfactor = modfactor;
		}

		public long next() {
			for (;;) {
				long n = previous * factor;
				n %= MAGIC;
				previous = n;
				if (n % modfactor == 0)
					return n;
			}
		}
	}

	@Override
	public void run(String input, int part, Output out) {

		if (part == 0) {
			long start = System.currentTimeMillis();
			Generator genA = new Generator(16807, getStart(input, "Generator A"), 1);
			Generator genB = new Generator(48271, getStart(input, "Generator B"), 1);
			int count = 0;
			for (int n = 0; n < 40e6; ++n) {
				long a = genA.next();
				long b = genB.next();

				boolean fail = false;
				for (int i = 0; i < 16; ++i) {
					if ((a & 0x1) != (b & 0x1)) {
						fail = true;
						break;
					}
					a >>= 1;
					b >>= 1;
				}
				if (!fail)
					++count;

			}
			out.println("Part 1: Succeeded: " + count + " in " + (System.currentTimeMillis() - start) + " ms");
		} else if (part == 1) {
			long start = System.currentTimeMillis();
			Generator genA = new Generator(16807, getStart(input, "Generator A"), 4);
			Generator genB = new Generator(48271, getStart(input, "Generator B"), 8);
			int count = 0;
			for (int n = 0; n < 5e6; ++n) {
				long a = genA.next();
				long b = genB.next();

				boolean fail = false;
				for (int i = 0; i < 16; ++i) {
					if ((a & 0x1) != (b & 0x1)) {
						fail = true;
						break;
					}
					a >>= 1;
					b >>= 1;
				}
				if (!fail)
					++count;

			}
			out.println("Part 2: Succeeded: " + count + " in " + (System.currentTimeMillis() - start) + " ms");
		}

	}

	private long getStart(String input, String find) {
		String[] lines = input.split("\n");
		for (String l : lines) {
			if (l.startsWith(find)) {
				String[] bits = l.split(" ");
				return Long.parseLong(bits[bits.length - 1]);
			}
		}
		throw new RuntimeException("Not found: " + find);
	}

}

package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.StringIter;

public class Day16 extends AoCBase {

	public static void main(String[] args) {
		new Day16().run(SourceData.get(16), 0, new ConsoleOutput());
		new Day16().run(SourceData.get(16), 1, new ConsoleOutput());
	}

	public abstract static class Op {
		public abstract void apply(char[] array);
	}

	public static class Spin extends Op {
		private final int count;

		public Spin(int count) {
			super();
			this.count = count;
		}

		@Override
		public void apply(char[] array) {
			char[] tmp = new char[array.length];
			for (int i = 0; i < array.length; ++i)
				tmp[(i + count) % array.length] = array[i];
			System.arraycopy(tmp, 0, array, 0, array.length);
		}

		@Override
		public String toString() {
			return "Spin [count=" + count + "]";
		}

	}

	public static class Exchange extends Op {
		private final int	a;
		private final int	b;

		public Exchange(int a, int b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public void apply(char[] array) {
			char tmp = array[a];
			array[a] = array[b];
			array[b] = tmp;
		}

		@Override
		public String toString() {
			return "Exchange [a=" + a + ", b=" + b + "]";
		}

	}

	public static class Partner extends Op {
		private final char	a;
		private final char	b;

		public Partner(char a, char b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public void apply(char[] array) {
			int posA = -1;
			int posB = -1;
			for (int i = 0; i < array.length; ++i) {
				if (array[i] == a)
					posA = i;
				if (array[i] == b)
					posB = i;
			}

			if (posA == -1 || posB == -1)
				throw new RuntimeException("Could not find one of the programs in the list");

			char tmp = array[posA];
			array[posA] = array[posB];
			array[posB] = tmp;
		}

		@Override
		public String toString() {
			return "Partner [a=" + a + ", b=" + b + "]";
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		char[] programs = new char[16];
		for (int i = 0; i < programs.length; ++i)
			programs[i] = (char) ('a' + i);

		List<Op> ops = parse(input);
		//ops.forEach(e -> out.println(e.toString()));

		if (part == 0) {
			ops.forEach(e -> e.apply(programs));
			out.println("Part 1: " + new String(programs));
		} else if (part == 1) {
			//
			// Running it a billion times is impractical, so instead we'll look 
			// for when a configuration repeats, and then we can use that to bypass
			// most of the dance
			//
			String start = new String(programs);
			int n = 0;
			while (n < 1e9) {
				ops.forEach(e -> e.apply(programs));
				++n;
				String cur = new String(programs);
				out.println(n + ": " + cur);
				if (cur.equals(start)) {
					out.println("Repeated at " + n);
					break;
				}
			}
			int cycle = n;
			out.println("Part 2: cycle length is " + cycle);

			System.arraycopy(start.toCharArray(), 0, programs, 0, programs.length); // reset
			n = (int) (1e9 % cycle);
			out.println("Part 2: Running another " + n + " dances");
			while (n-- > 0)
				ops.forEach(e -> e.apply(programs));
			out.println("Part 2: " + new String(programs));
		}
	}

	private List<Op> parse(String input) {
		List<Op> lst = new ArrayList<>();
		StringIter iter = new StringIter(input);
		for (;;) {
			char ch = iter.next();
			int a;
			int b;
			switch (ch) {
				case 'x':
					a = readInt(iter);
					if (iter.next() != '/')
						throw new RuntimeException("Expected a / at position " + iter.pos());
					b = readInt(iter);
					lst.add(new Exchange(a, b));
					break;
				case 's':
					lst.add(new Spin(readInt(iter)));
					break;
				case 'p':
					char ca = iter.next();
					if (iter.next() != '/')
						throw new RuntimeException("Expected a / at position " + iter.pos());
					char cb = iter.next();
					lst.add(new Partner(ca, cb));
					break;

				default:
					throw new RuntimeException("Unknown op: " + ch + " at position " + iter.pos());
			}
			if (iter.eof())
				break;
			if (iter.next() != ',')
				throw new RuntimeException("Expected a , at position " + iter.pos());
		}

		return lst;
	}

	private int readInt(StringIter iter) {
		int n = 0;
		for (;;) {
			if (iter.eof())
				return n;
			char ch = iter.peek();
			if (ch > '9' || ch < '0')
				return n;
			n = n * 10 + (ch - '0');
			iter.next();
		}
	}

	private String render(char[] programs) {
		return new String(programs);
	}

}

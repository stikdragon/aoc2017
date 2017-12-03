package uk.co.stikman.aoc.year2016;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.StringIter;

public class Day9 extends AoCBase {

	public static void main(String[] args) {
		new Day9().run(SourceData.get(9), 0, new ConsoleOutput());
		new Day9().run(SourceData.get(9), 1, new ConsoleOutput());
	}

	private interface StringOut {
		void append(String s);

		void append(char c);

		long length();

		String first(int i);
	}

	private static class BufferBackedStringOut implements StringOut {
		private StringBuilder buf = new StringBuilder();

		@Override
		public void append(String s) {
			buf.append(s);
		}

		@Override
		public void append(char c) {
			buf.append(c);
		}

		@Override
		public String toString() {
			return buf.toString();
		}

		@Override
		public long length() {
			return buf.length();
		}

		@Override
		public String first(int i) {
			return buf.toString().substring(0, i);
		}
	}

	private static class CountingStringOut implements StringOut {
		private long count;

		@Override
		public void append(String s) {
			count += s.length();
		}

		@Override
		public void append(char c) {
			++count;
		}

		@Override
		public String toString() {
			return "";
		}

		@Override
		public long length() {
			return count;
		}

		@Override
		public String first(int i) {
			return "";
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		StringIter iter = new StringIter(input);
		StringOut res = part == 0 ? new BufferBackedStringOut() : new CountingStringOut();
		while (!iter.eof()) {
			char ch = iter.peek();
			if (ch == '(') {
				processRepeat(iter, res, part == 1);
			} else {
				res.append(iter.next());
			}
		}
		String s = "...";
		if (res.length() < 1024)
			s = res.toString();
		else
			s = res.first(1024) + "...";
		out.println("Result = " + s);
		out.println("That's " + res.length() + " characters");

	}

	private void processRepeat(StringIter iter, StringOut res, boolean recursive) {
		if (iter.next() != '(')
			throw new IllegalStateException("Must start with (");
		int len = 0;
		int cnt = 0;
		boolean b = false;
		while (!iter.eof()) {
			char ch = iter.next();
			if (ch == 'x') {
				b = true;
			} else if (ch == ')') {
				//
				// execute it
				//
				StringBuilder sb = new StringBuilder();
				while (!iter.eof() && len-- > 0)
					sb.append(iter.next());
				String s = sb.toString();

				if (recursive) {
					while (cnt-- > 0) {
						StringIter i2 = new StringIter(s);
						while (!i2.eof()) {
							char ch2 = i2.peek();
							if (ch2 == '(') {
								processRepeat(i2, res, recursive);
							} else {
								res.append(i2.next());
							}
						}
					}
				} else { // nice and simple
					while (cnt-- > 0)
						res.append(s);
				}
				return;
			} else {
				if (b)
					cnt = cnt * 10 + (ch - '0');
				else
					len = len * 10 + (ch - '0');
			}
		}
	}

}

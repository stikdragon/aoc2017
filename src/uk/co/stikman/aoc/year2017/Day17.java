package uk.co.stikman.aoc.year2017;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.IntList;
import uk.co.stikman.aoc.utils.Output;

public class Day17 extends AoCBase {

	public static void main(String[] args) {
		new Day17().run(SourceData.get(17), 0, new ConsoleOutput());
		new Day17().run(SourceData.get(17), 1, new ConsoleOutput());
	}

	private static class Element {
		Element	next;
		int		val;

		public Element(int val) {
			super();
			this.val = val;
		}

		@Override
		public String toString() {
			return Integer.toString(val);
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		int stepsize = Integer.parseInt(input);
		IntList lst = new IntList();
		lst.add(0);

		if (part == 0) {
			Element start = new Element(0);
			start.next = start;
			Element cur = start;
			for (int i = 0; i < 2017; ++i) {
				for (int j = 0; j < stepsize; ++j)
					cur = cur.next;
				Element x = new Element(i + 1);
				Element n = cur.next;
				cur.next = x;
				x.next = n;
				cur = x;
			}
			
			out.println(printList(start));

			cur = start;
			for (int i = 0; i < 2017; ++i) {
				if (cur.val == 2017) {
					cur = cur.next;
					out.println("Part 1: Next value is: " + cur.val);
					return;
				}
				cur = cur.next;
			}

		} else {
			Element start = new Element(0);
			start.next = start;
			Element cur = start;
			for (int i = 0; i < 50e6; ++i) {
				for (int j = 0; j < stepsize; ++j)
					cur = cur.next;
				Element x = new Element(i + 1);
				Element n = cur.next;
				cur.next = x;
				x.next = n;
				cur = x;
				if (i % 1000000 == 0)
					out.println(i + " of 50 million");
			}

			cur = start;
			for (int i = 0; i < 50e6; ++i) {
				if (cur.val == 0) {
					cur = cur.next;
					out.println("Part 2: Next value is: " + cur.val);
					return;
				}
				cur = cur.next;
			}
		}
	}

	private String printList(Element start) {
		Element e = start;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			sb.append(e.val).append(", ");
			e = e.next;
			if (e == start)
				break;
		}
		return sb.toString();
	}

}

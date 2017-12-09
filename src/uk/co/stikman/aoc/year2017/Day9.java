package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.MutableInt;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.StringIter;

public class Day9 extends AoCBase {

	public static class StreamError extends Exception {
		private static final long serialVersionUID = 1L;

		public StreamError(StringIter iter, String msg) {
			super("Malformed stream error at position " + iter.pos() + ": " + msg);
		}
	}

	public static void main(String[] args) {
		new Day9().run(SourceData.get(9), 0, new ConsoleOutput());
		new Day9().run(SourceData.get(9), 1, new ConsoleOutput());
	}

	private static class Group {
		private String		text;
		private List<Group>	groups	= new ArrayList<>();
		private Group		parent;
		private int			position;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public List<Group> getGroups() {
			return groups;
		}

		public int score() {
			if (parent == null)
				return 1;
			return parent.score() + 1;
		}

		public Group getParent() {
			return parent;
		}

		public void setParent(Group parent) {
			this.parent = parent;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		@Override
		public String toString() {
			return "Group starts at " + position + "  score = " + score();
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		try {
			StringIter iter = new StringIter(input);
			MutableInt charcounter = new MutableInt();
			Group root = readGroup(iter, charcounter);

			long sum = 0;
			LinkedList<Group> open = new LinkedList<>();
			open.add(root);
			while (!open.isEmpty()) {
				Group g = open.removeFirst();
				sum += g.score();
				open.addAll(g.getGroups());
			}

			if (part == 0)
				out.println("Part 1: Sum == " + sum);
			if (part == 1)
				out.println("Part 2: Garbage chars == " + charcounter.getN());

//			printTree(out, root, 0);

		} catch (StreamError e) {
			throw new RuntimeException(e);
		}

	}

	private void printTree(Output out, Group root, int depth) {
		char[] arr = new char[depth * 2];
		for (int i = 0; i < arr.length; ++i)
			arr[i] = ' ';
		out.println(new String(arr) + root);
		for (Group g : root.getGroups())
			printTree(out, g, depth + 1);
	}

	private Group readGroup(StringIter iter, MutableInt charcounter) throws StreamError {
		Group result = new Group();
		result.setPosition(iter.pos());
		if (iter.next() != '{')
			throw new StreamError(iter, "Malformed stream, expected {");

		while (!iter.eof()) {
			char ch = iter.peek();
			if (ch == '<') {
				readGarbage(iter, charcounter);
			} else if (ch == '{') {
				Group g = readGroup(iter, charcounter);
				result.getGroups().add(g);
				g.setParent(result);
			} else if (ch == ',') {
				iter.next(); // ok
			} else if (ch == '}') {
				iter.next();
				return result;
			} else {
				throw new StreamError(iter, "Malformed stream, unexpected character: " + ch);
			}
		}
		throw new StreamError(iter, "Malformed stream, end of stream");
	}

	private void readGarbage(StringIter iter, MutableInt charcounter) throws StreamError {
		//
		// read until end of garbage >
		//
		if (iter.next() != '<')
			throw new StreamError(iter, "Malformed stream, expected <");

		while (!iter.eof()) {
			char ch = iter.peek();
			if (ch == '!') {
				iter.next(); // skip
				iter.next();
			} else if (ch == '>') {
				iter.next();
				return;
			} else {
				charcounter.inc(1);
				iter.next();
			}
		}
	}
}

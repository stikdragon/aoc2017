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

public class Day14 extends AoCBase {

	public static void main(String[] args) {
		new Day14().run(SourceData.get(14), 0, new ConsoleOutput());
		new Day14().run(SourceData.get(14), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output out) {
		input = input.trim();
		List<String> hashes = new ArrayList<>();
		for (int i = 0; i < 128; ++i)
			hashes.add(Day10.knot(input + "-" + i));
		if (part == 0) {
			//
			// count bits set
			//
			int sum = 0;
			for (String hash : hashes)
				for (char ch : hash.toCharArray())
					sum += Integer.bitCount(Util.parseHex(ch));
			out.println("Part 1: Bits set = " + sum);
		} else {
			//
			// easiest to just render them 
			//
			TextPainter tp = new TextPainter();
			for (int y = 0; y < hashes.size(); ++y) {
				String hash = hashes.get(y);
				for (int x = 0; x < hash.length(); ++x) { // will be from 0..32
					int c = Util.parseHex(hash.charAt(x));
					for (int n = 0; n < 4; ++n) {
						tp.putChar(x * 4 + 3 - n, y, (c % 2 == 1) ? '#' : '.');
						c >>= 1;
					}
				}
			}

			//
			// count regions, each time find an occupied space and find all 
			// connected ones, removing them as we go
			//
			int count = 0;
			for (;;) {
				boolean found = false;
				for (int y = 0; y < tp.getCurrentHeight(); ++y) {
					for (int x = 0; x < tp.getCurrentWidth(); ++x) {
						if (tp.getChar(x, y) == '#') {
							++count;
							found = true;
							removeGroup(tp, x, y);
						}
					}
				}
				if (!found) // finished
					break;
			}

			out.println(tp.toString());
			out.println("There are " + count + " groups");
		}
	}

	private void removeGroup(TextPainter tp, int x, int y) {
		if (x < 0 || y < y || x >= tp.getCurrentWidth() || y >= tp.getCurrentHeight())
			return;
		if (tp.getChar(x, y) != '#')
			return; // nothing to do here
		tp.putChar(x, y, '-');
		removeGroup(tp, x + 1, y);
		removeGroup(tp, x - 1, y);
		removeGroup(tp, x, y + 1);
		removeGroup(tp, x, y - 1);
	}

}

package uk.co.stikman.aoc.year2016;

import java.util.Arrays;
import java.util.LinkedList;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.Util;

public class Day14 extends AoCBase {
	public static void main(String[] args) {
		new Day14().run(SourceData.get(14), 0, new ConsoleOutput());
		new Day14().run(SourceData.get(14), 1, new ConsoleOutput());
	}

	private static final class Hash {
		private String		hash;
		private int			index;
		private boolean[]	fives	= new boolean[55];

		public Hash(int idx, String hash) {
			this.hash = hash;
			this.index = idx;
			//
			// find any sequences of 5
			//
			char[] arr = hash.toCharArray();
			int cnt = 1;
			char cur = arr[0];
			for (int i = 1; i < arr.length; ++i) {
				if (arr[i] == cur) {
					if (++cnt == 5)
						fives[cur - '0'] = true;

				} else {
					cnt = 1;
					cur = arr[i];
				}
			}
		}

		public String getHash() {
			return hash;
		}

		public boolean hasFive(char ch) {
			return fives[ch - '0'];
		}

		public char findSequenceOfThree() {
			char[] arr = hash.toCharArray();
			int cnt = 1;
			char cur = arr[0];
			for (int i = 1; i < arr.length; ++i) {
				if (arr[i] == cur) {
					if (++cnt == 3)
						return cur;
				} else {
					cnt = 1;
					cur = arr[i];
				}
			}

			return 0;
		}

		@Override
		public String toString() {
			String s = "";
			String sep = "";
			for (char ch = '0'; ch < '9'; ++ch) {
				if (hasFive(ch)) {
					s += sep + ch;
					sep = ", ";
				}
			}
			for (char ch = 'a'; ch < 'f'; ++ch) {
				if (hasFive(ch)) {
					s += sep + ch;
					sep = ", ";
				}
			}

			return "Hash [index=" + index + ", hash=" + hash + ", fives=" + s + "]";
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		String salt = input.trim();
		//		salt = "abc";

		LinkedList<Hash> list = new LinkedList<Hash>();
		LinkedList<Hash> results = new LinkedList<Hash>();
		Hash sixtyforth = null;
		int idx = -1;
		outer: while (true) {
			++idx;
			String test;
			if (part == 0)
				test = Util.md5(salt + idx);
			else
				test = stretchHash(salt + idx, 2016);
			Hash hash = new Hash(idx, test);
			list.add(hash);
			if (list.size() >= 1001) {
				Hash first = list.removeFirst();
				char three = first.findSequenceOfThree();
				if (three == 0) // not valid one
					continue;
				for (Hash h : list) { // check the next 1000 ones
					if (h.hasFive(three)) {
						results.add(hash);
						if (results.size() == 64) {
							sixtyforth = first;
							break outer;
						}
						break;
					}
				}
			}
		}

		out.println("Part " + (part + 1) + ": 64th hash is: " + sixtyforth);

	}

	private String stretchHash(String start, int count) {
		++count;
		while (count-- > 0)
			start = Util.md5(start);
		return start;
	}

}

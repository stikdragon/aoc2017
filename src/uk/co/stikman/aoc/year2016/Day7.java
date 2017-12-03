package uk.co.stikman.aoc.year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day7 extends AoCBase {
	public static void main(String[] args) {
		new Day7().run(SourceData.get(7), 0, new ConsoleOutput());
		new Day7().run(SourceData.get(7), 1, new ConsoleOutput());
	}

	private static final class Fragment {
		private final String	text;
		private final boolean	hyper;

		public Fragment(String text, boolean hyper) {
			super();
			this.text = text;
			this.hyper = hyper;
		}

		public String getText() {
			return text;
		}

		public boolean isHyper() {
			return hyper;
		}

		@Override
		public String toString() {
			if (hyper)
				return "[" + text + "]";
			return text;
		}

	}

	private static final class Address {
		private List<Fragment> fragments;

		public Address(String addr) {
			parse(addr);
		}

		private void parse(String addr) {
			fragments = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			boolean b = false;
			for (int i = 0; i < addr.length(); ++i) {
				char ch = addr.charAt(i);
				if (ch == '[') {
					fragments.add(new Fragment(sb.toString(), false));
					sb = new StringBuilder();
					b = true;
				} else if (ch == ']') {
					fragments.add(new Fragment(sb.toString(), true));
					sb = new StringBuilder();
					b = false;
				} else {
					sb.append(ch);
				}
			}
			if (sb.length() > 0)
				fragments.add(new Fragment(sb.toString(), b));

		}

		@Override
		public String toString() {
			return fragments.toString();
		}

		public List<Fragment> getFragments() {
			return fragments;
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		List<Address> lines = Arrays.stream(input.split("\n")).map(String::trim).map(Address::new).collect(Collectors.toList());
		if (part == 0) {
			int count = 0;
			for (Address addr : lines) {
				//
				// move along the string, testing for ABBA thing on all fragments
				//
				boolean b = false;
				boolean q = false;
				for (Fragment f : addr.getFragments()) {
					char[] arr = f.getText().toCharArray();
					for (int i = 0; i < arr.length - 3; ++i) {
						if (testAbba(arr, i)) {
							if (f.isHyper())
								q |= true;
							else
								b |= true;
						}
					}
				}
				if (b & !q)
					++count;
			//	out.println((b ? "YES " : "NO  ") + addr);
			}
			out.println("Part 1: " + count + " addresses are ABBA");
		} else if (part == 1) {
			int count = 0;
			for (Address addr : lines) {
				boolean b = false;
				//
				// Looking for ABA and BAB in the normal and bracketed (hyper) bit
				//
				for (Fragment f : addr.getFragments()) {
					if (f.isHyper())
						continue;
					char[] arr = f.getText().toCharArray();
					for (int i = 0; i < arr.length - 2; ++i) {
						if (findAba(arr, i)) {
							//
							// now check the other hyper frags for corresponding string
							//
							char[] tmp = new char[3];
							tmp[0] = tmp[2] = arr[i + 1];
							tmp[1] = arr[i];
							String seek = new String(tmp);
							boolean q = false;
							for (Fragment f2 : addr.getFragments()) {
								if (!f2.isHyper())
									continue;
								b |= f2.getText().indexOf(seek) != -1;
							}
							if (b)
								break;
						}
					}
				}

				if (b)
					++count;
				out.println((b ? "YES " : "NO  ") + addr);
			}
			
			out.println("Part 2: " + count + " support SSL");
			
		} else
			throw new RuntimeException("Part.. ?");

	}

	private static final boolean findAba(char[] arr, int offset) {
		return arr[offset] == arr[offset + 2] && arr[offset + 1] != arr[offset];
	}

	private static final boolean testAbba(char[] arr, int offset) {
		return arr[offset] == arr[offset + 3] && arr[offset + 1] == arr[offset + 2] && arr[offset] != arr[offset + 1];
	}

}

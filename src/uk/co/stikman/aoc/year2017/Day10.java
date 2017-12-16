package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.MutableInt;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.StringIter;

public class Day10 extends AoCBase {

	public static void main(String[] args) {
		new Day10().run(SourceData.get(10), 0, new ConsoleOutput());
		new Day10().run(SourceData.get(10), 1, new ConsoleOutput());
	}

	public static class CircleList<T> {
		private final Object[] elements;

		public CircleList(int size) {
			elements = new Object[size];
		}

		private final int makeIndex(int in) {
			while (in < 0)
				in += elements.length;
			return in % elements.length;
		}

		public void set(int idx, T val) {
			elements[makeIndex(idx)] = val;
		}

		@SuppressWarnings("unchecked")
		public T get(int idx) {
			return (T) elements[makeIndex(idx)];
		}

		public List<T> sublist(int start, int len) {
			//
			// we could do this in a cleverer way and have a view, but this is quicker to implement
			//
			ArrayList<T> res = new ArrayList<>();
			while (len-- > 0)
				res.add(get(start++));
			return res;
		}

		public int size() {
			return elements.length;
		}

		@Override
		public String toString() {
			return Arrays.toString(elements);
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		CircleList<Integer> list = new CircleList<>(256);
		for (int i = 0; i < list.size(); ++i)
			list.set(i, i);

		if (part == 0) {
			List<Integer> lengths = Arrays.stream(input.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
			int offset = 0;
			int skip = 0;
			for (int length : lengths) {
				List<Integer> tmp = list.sublist(offset, length);
				for (int i = 0; i < length; ++i)
					list.set(offset + length - i - 1, tmp.get(i));
				//				out.println(list.toString());
				offset += length + skip++;
			}
			out.println("Part 1: Product of first two is = " + list.get(0) * list.get(1));
		} else {
			out.println("Part 2: Hash = " + knot(input));
		}

	}

	public static String knot(String input) {
		CircleList<Integer> list = new CircleList<>(256);
		for (int i = 0; i < list.size(); ++i)
			list.set(i, i);

		List<Integer> lengths = new ArrayList<>();
		for (char ch : input.toCharArray())
			lengths.add((int) ch);
		lengths.add(17);
		lengths.add(31);
		lengths.add(73);
		lengths.add(47);
		lengths.add(23);

		int offset = 0;
		int skip = 0;
		for (int round = 0; round < 64; ++round) {
			for (int length : lengths) {
				List<Integer> tmp = list.sublist(offset, length);
				for (int i = 0; i < length; ++i)
					list.set(offset + length - i - 1, tmp.get(i));
				offset += length + skip++;
			}
		}

		//
		// reduce list to 16 elements
		//
		int[] arr = new int[list.size() / 16];
		for (int i = 0; i < 16; ++i) {
			int accum = list.get(i * 16);
			for (int j = 1; j < 16; ++j)
				accum ^= list.get(i * 16 + j);
			arr[i] = accum;
		}

		StringBuilder sb = new StringBuilder();
		for (int i : arr) {
			if (i < 16)
				sb.append("0");
			sb.append(Integer.toHexString(i));
		}

		return sb.toString();
	}

}

package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day4 extends AoCBase {

	public static void main(String[] args) {
		new Day4().run(SourceData.get(4), 0, new ConsoleOutput());
		new Day4().run(SourceData.get(4), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output output) {
		List<String> lines = Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());
		if (part == 0) {
			int cnt = 0;
			for (String line : lines) {
				Set<String> bits = new HashSet<>();
				List<String> lst = Arrays.asList(line.split(" "));
				bits.addAll(lst);
				if (bits.size() == lst.size())
					++cnt;
			}

			output.println("There are " + cnt + " valid passphrases");
		}
		if (part == 1) {
			int cnt = 0;
			for (String line : lines) {
				Set<String> bits = new HashSet<>();
				List<String> lst = Arrays.asList(line.split(" ")).stream().map(s -> sortLetters(s)).collect(Collectors.toList());
				bits.addAll(lst);
				if (bits.size() == lst.size())
					++cnt;
			}

			output.println("There are " + cnt + " valid passphrases with anagrams");
		}
	}

	private static String sortLetters(String s) {
		List<Character> lst = new ArrayList();
		for (char ch : s.toCharArray())
			lst.add(ch);
		lst.sort((a, b) -> Character.compare(a, b));
		char[] arr = new char[lst.size()];
		int i = 0;
		for (char ch : lst)
			arr[i++] = ch;
		return new String(arr);
	}

}

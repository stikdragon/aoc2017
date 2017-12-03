package uk.co.stikman.aoc.year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day6 extends AoCBase {
	public static void main(String[] args) {
		new Day6().run(SourceData.get(6), 0, new ConsoleOutput());
		new Day6().run(SourceData.get(6), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output out) {
		List<String> lines = Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());
		int size = lines.stream().map(String::length).mapToInt(n -> n).max().orElse(0);
		List<Map<Character, Integer>> counts = new ArrayList<>();
		for (int i = 0; i < size; ++i)
			counts.add(new HashMap<>());
		for (String line : lines) {
			for (int i = 0; i < size; ++i) {
				Map<Character, Integer> map = counts.get(i);
				char ch = line.charAt(i);
				Integer cnt = map.get(ch);
				if (cnt == null)
					map.put(ch, 1);
				else
					map.put(ch, cnt + 1);
			}
		}

		char[] res = new char[size];
		for (int i = 0; i < size; ++i) {
			Map<Character, Integer> map = counts.get(i);
			if (part == 0)
				res[i] = map.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()).findFirst().orElseThrow(RuntimeException::new).getKey();
			else if (part == 1)
				res[i] = map.entrySet().stream().sorted((a, b) -> a.getValue() - b.getValue()).findFirst().orElseThrow(RuntimeException::new).getKey();
		}
		out.println("Recovered password is: " + new String(res));

	}

}

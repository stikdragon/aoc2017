package uk.co.stikman.aoc.year2016;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day12 extends AoCBase {
	public static void main(String[] args) {
		new Day12().run(SourceData.get(12), 0, new ConsoleOutput());
		new Day12().run(SourceData.get(12), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output out) {
		List<String> lines = Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());
			// 
			// instructions are CPY n/r r, INC r, DEC r, JMP n/r n/r
			//
			int[] registers = new int[4];
			if (part == 1)
				registers[2] = 1; // init c to 1
			
			int ptr = 0;
			while (ptr < lines.size()) {
				String[] bits = lines.get(ptr++).split(" ");
				if ("cpy".equals(bits[0])) {
					registers[bits[2].charAt(0) - 'a'] = getOperand(bits[1], registers);
				} else if ("inc".equals(bits[0])) {
					registers[bits[1].charAt(0) - 'a']++;
				} else if ("dec".equals(bits[0])) {
					registers[bits[1].charAt(0) - 'a']--;
				} else if ("jnz".equals(bits[0])) {
					//
					// if first bit isn't zero then it jumps to relative bits[2]
					//
					int one = getOperand(bits[1], registers);
					if (one != 0) {
						int two = getOperand(bits[2], registers);
						--ptr; // we've alreayd offset this so wind it back
						ptr += two;
					}
				}
			}
			out.println("Part " + part + " register contents: " + Arrays.toString(registers));

	}

	private static final int getOperand(String s, int[] registers) {
		if (Character.isDigit(s.charAt(0)) || s.charAt(0) == '-')
			return Integer.parseInt(s);
		else
			return registers[s.charAt(0) - 'a'];
	}

}

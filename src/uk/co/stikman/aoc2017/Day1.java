package uk.co.stikman.aoc2017;

public class Day1 extends AoCBase {

	public static void main(String[] args) {
		new Day1().run(SourceData.get(1), 0, new ConsoleOutput());
		new Day1().run(SourceData.get(1), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output output) {
		long sum = 0;
		for (int idx = 0; idx < input.length(); ++idx) {
			char cur = input.charAt(idx);
			int nextidx = idx + (part == 0 ? 1 : (input.length() / 2));
			while (nextidx >= input.length())
				nextidx -= input.length();
			char next = input.charAt(nextidx);
			if (cur == next)
				sum += cur - '0';
		}
		output.println(Long.toString(sum));
	}

}

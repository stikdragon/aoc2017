package uk.co.stikman.aoc.year2016;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day16 extends AoCBase {

	public static void main(String[] args) {
		new Day16().run(SourceData.get(16), 0, new ConsoleOutput());
		new Day16().run(SourceData.get(16), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output out) {
		char[] current = input.trim().toCharArray();

		int fillSize = part == 0 ? 272 : 35651584;

		while (current.length < fillSize) {
			char[] nu = new char[current.length * 2 + 1];
			System.arraycopy(current, 0, nu, 0, current.length);
			nu[current.length] = '0';
			for (int i = 0; i < current.length; ++i)
				nu[nu.length - i - 1] = nu[i] == '1' ? '0' : '1';
			current = nu;
		}

		char[] check = checksum(current, fillSize);
		out.println("Part " + part + " Data: " + safeString(current));
		out.println("Part " + part + " Checksum: " + safeString(check));
	}

	private static final String safeString(char[] arr) {
		if (arr.length > 512)
			return new String(arr, 0, 512) + "...";
		return new String(arr);
	}

	private char[] checksum(char[] buf, int len) {
		char[] res = new char[len / 2];
		for (int i = 0; i < len; i += 2)
			res[i / 2] = (buf[i] == buf[i + 1]) ? '1' : '0';
		if (res.length % 2 == 0)
			return checksum(res, len / 2);
		return res;
	}

}

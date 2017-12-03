package uk.co.stikman.aoc.year2016;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day5 extends AoCBase {
	public static void main(String[] args) {
		new Day5().run(SourceData.get(5), 0, new ConsoleOutput());
		new Day5().run(SourceData.get(5), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output out) {
		String doorid = input.trim();
		char[] password = new char[8];
		int count = 0;
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Can't get MD5 instance");
		}
		for (int i = 0; i < Integer.MAX_VALUE; ++i) {
			String test = doorid + i;
			md5.update(test.getBytes(StandardCharsets.US_ASCII));
			byte[] res = md5.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : res) {
				if ((b & 0xff) <= 0xf)
					sb.append('0');
				sb.append(Integer.toHexString(b & 0xff));
			}
			String hex = sb.toString();
			if (hex.startsWith("00000")) {
				if (part == 0) {
					password[count++] = hex.charAt(5);
					if (count >= 8) {
						out.println("Part 1: Password is: " + new String(password));
						return;
					}
				} else if (part == 1) {
					int pos = hex.charAt(5) - '0';
					if (pos < 8) {
						if (password[pos] == 0) {
							password[pos] = hex.charAt(6);
							++count;
							if (count >= 8) {
								out.println("Part 2: Password is: " + new String(password));
								return;
							}
						}
					}
				}
			}
		}
	}
}

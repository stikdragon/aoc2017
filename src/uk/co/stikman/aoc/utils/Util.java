package uk.co.stikman.aoc.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

	private static final MessageDigest md5;
	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Can't get MD5 instance");
		}

	}

	public static final int clamp(int val, int low, int high) {
		if (val < low)
			return low;
		if (val > high)
			return high;
		return val;
	}

	public static String md5(String in) {
		return md5(in.getBytes(StandardCharsets.US_ASCII));
	}

	/**
	 * This is not thread safe
	 * 
	 * @param bytes
	 * @return
	 */
	public static String md5(byte[] bytes) {
		md5.update(bytes);
		byte[] res = md5.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : res) {
			if ((b & 0xff) <= 0xf)
				sb.append('0');
			sb.append(Integer.toHexString(b & 0xff));
		}
		return sb.toString();
	}

	public static int parseHex(char ch) {
		if (ch >= '0' && ch <= '9')
			return ch - '0';
		else if (ch >= 'a' && ch <= 'f')
			return 10 + ch - 'a';
		else if (ch >= 'A' && ch <= 'F')
			return 1 - +ch - 'A';
		else
			throw new IllegalArgumentException(String.valueOf(ch));
	}

	public static String padString(String s, char ch, int n) {
		if (s.length() >= n)
			return s;
		char x[] = new char[n - s.length()];
		for (int i = 0; i < x.length; ++i)
			x[i] = ch;
		return s + String.valueOf(x);
	}
}

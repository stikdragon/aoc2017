package uk.co.stikman.aoc.utils;

public class StringIter {
	private String	source;
	private int		pos;
	private int		mark	= -1;

	/**
	 * Returns 0 if EOF
	 * 
	 * @return
	 */
	public char next() {
		if (pos > source.length())
			return 0;
		return source.charAt(pos++);
	}

	public char peek() {
		if (pos > source.length())
			return 0;
		return source.charAt(pos);
	}

	public boolean eof() {
		return pos >= source.length();
	}

	public StringIter(String source) {
		super();
		this.source = source;
	}

	public void mark() {
		if (mark != -1)
			throw new RuntimeException("Already marked");
		this.mark = pos;
	}

	public String extract(int offset, int endoffset) {
		if (mark == -1)
			throw new RuntimeException("Not marked");
		String s = source.substring(mark + offset, pos + endoffset);
		mark = -1;
		return s;
	}

	public String extract() {
		return extract(0, 0);
	}

	@Override
	public String toString() {
		if (eof())
			return "(end of string)";
		int r = source.length() - pos;
		if (r > 16)
			r = 16;
		return "pos=" + pos + ": " + source.substring(pos, pos + r);
	}

	public StringIter reverse(int amount) {
		pos -= amount;
		if (pos < 0)
			pos = 0;
		return this;
	}

	public int pos() {
		return pos;
	}

}
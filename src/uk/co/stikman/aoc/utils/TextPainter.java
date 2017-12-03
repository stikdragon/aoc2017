package uk.co.stikman.aoc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lets you address an infinite canvas with text characters in
 * 
 * @author Stik
 *
 */
public class TextPainter {
	public enum BoxSet {
		SINGLE, DOUBLE
	}

	public static class Rect {
		public int	left;
		public int	top;
		public int	width;
		public int	height;

		public Rect(int left, int top, int width, int height) {
			super();
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}

		public Rect() {
		}

		public boolean containsY(int y) {
			return y >= top && y < top + height;
		}

		public boolean containsX(int x) {
			return x >= left && x < left + width;
		}

		public boolean contains(int x, int y) {
			return containsX(x) && containsY(y);
		}

	}

	public static class Line {
		private char[]	data;
		private int		len	= 0;

		@Override
		public String toString() {
			if (data == null)
				return "";
			return new String(data, 0, len);
		}

		public void set(int index, char ch) {
			if (index >= len)
				setLength(index + 1);
			data[index] = ch;
		}

		private void setLength(int len) {
			if (len == 0) {
				this.len = len;
				return;
			}
			//
			// reallocate
			//
			if (data == null) {
				data = new char[64];
				for (int i = 0; i < 64; ++i)
					data[i] = ' ';
			}
			int x = data.length;
			while (x < len)
				x <<= 1;
			char[] n = new char[x];
			System.arraycopy(data, 0, n, 0, this.len);
			for (int i = this.len; i < x; ++i)
				n[i] = ' ';
			this.data = n;
			this.len = len;
		}

		/**
		 * this always returns something, if it's out of range then it returns a
		 * space character (32)
		 * 
		 * @param x
		 * @return
		 */

		public char getChar(int x) {
			if (x >= len)
				return ' ';
			return data[x];
		}
	}

	private List<Line>	area		= new ArrayList<>();
	private Rect		clipRect	= null;

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Line s : area)
			sb.append(s).append("\n");
		return sb.toString();
	}

	public void putChar(int x, int y, char ch) {
		if (clipRect != null && !clipRect.contains(x, y))
			return;
		getLine(y).set(x, ch);
	}

	/**
	 * Ensures line <code>y</code> exists
	 * 
	 * @param y
	 */
	private Line getLine(int y) {
		while (area.size() <= y)
			area.add(new Line());
		return area.get(y);
	}

	public Rect put(int x, int y, String s) {
		return put(x, y, Arrays.asList(s.split("\n")));
	}

	public Rect put(int x, int y, Iterable<String> items) {
		Rect res = new Rect(x, y, 0, 0);
		int maxx = x;
		int maxy = y;
		int dy = 0;
		for (String t : items) {
			if (clipRect != null && !clipRect.containsY(y + dy))
				continue;
			Line l = getLine(y + dy);
			maxy = Math.max(y + dy, maxy);
			int i = x;
			for (char ch : t.toCharArray()) {
				if (clipRect != null && !clipRect.containsX(i))
					continue;
				l.set(i, ch);
				++i;
			}
			maxx = Math.max(i - 1, maxx);
			++dy;
		}
		res.width = maxx - res.left;
		res.height = maxy - res.top;
		return res;
	}

	public Rect putInBox(int x, int y, String s, String caption) {
		Rect res = new Rect();

		Rect r = put(x + 2, y + 1, s);
		res.left = x;
		res.top = y;
		res.width = r.width + 4;
		res.height = r.height + 2;
		if (caption.length() + 4 > res.width)
			res.width = caption.length() + 4;

		drawBox(res, caption);

		return res;
	}

	public Rect getClipRect() {
		return clipRect;
	}

	public void setClipRect(Rect clipRect) {
		this.clipRect = clipRect;
	}

	public int getCurrentHeight() {
		return area.size();
	}

	public int getCurrentWidth() {
		int x = 0;
		for (Line l : area)
			x = Math.max(x, l.len);
		return x;
	}

	private static final char[]	BOX_CHARS_SGL	= { ' ', ' ', ' ', '┘', ' ', '─', '└', '┴', ' ', '┐', '│', '┤', '┌', '┬', '├', '┼' };
	private static final char[]	BOX_CHARS_DBL	= { ' ', ' ', ' ', '╝', ' ', '═', '╚', '╩', ' ', '╗', '║', '╣', '╔', '╦', '╠', '╬' };
	private char[]				boxCharSet		= BOX_CHARS_SGL;

	/**
	 * caption can be <code>null</code>
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param caption
	 */
	public void drawBox(int x, int y, int w, int h, String caption) {
		for (int i = 1; i < w; ++i) {
			putBoxChar(x + i, y, boxCharSet[5]);
			putBoxChar(x + i, y + h, boxCharSet[5]);
		}
		for (int i = 1; i < h; ++i) {
			putBoxChar(x, i + y, boxCharSet[10]);
			putBoxChar(x + w, i + y, boxCharSet[10]);
		}
		putBoxChar(x, y, boxCharSet[12]);
		putBoxChar(x + w, y, boxCharSet[9]);
		putBoxChar(x, y + h, boxCharSet[6]);
		putBoxChar(x + w, y + h, boxCharSet[3]);
		if (caption != null && !caption.isEmpty())
			put(x + 2, y, " " + caption + " ");
	}

	/**
	 * @see #drawBox(int, int, int, int, String)
	 * @param r
	 * @param caption
	 */
	public void drawBox(Rect r, String caption) {
		drawBox(r.left, r.top, r.width, r.height, caption);
	}

	public void putBoxChar(int x, int y, char ch) {
		int nch = getBoxCharIndex(ch);
		if (nch == -1)
			return;
		if (clipRect != null && !clipRect.contains(x, y))
			return;
		Line l = getLine(y);
		char exist = l.getChar(x);
		int nexist = getBoxCharIndex(exist);
		if (nexist > 0)
			l.set(x, boxCharSet[nexist | nch]);
		else
			l.set(x, boxCharSet[nch]);
	}

	private int getBoxCharIndex(char ch) {
		for (int i = 0; i < boxCharSet.length; ++i)
			if (boxCharSet[i] == ch)
				return i;
		return -1;
	}

	public boolean isBoxChar(char ch) {
		return getBoxCharIndex(ch) != -1;
	}

	public char getChar(int x, int y) {
		if (y < 0 || y >= area.size())
			return ' ';
		if (x < 0)
			return ' ';
		return getLine(y).getChar(x);
	}

	public BoxSet getBoxSet() {
		if (boxCharSet == BOX_CHARS_SGL)
			return BoxSet.SINGLE;
		return BoxSet.DOUBLE;
	}

	public void setBoxSet(BoxSet set) {
		switch (set) {
			case DOUBLE:
				boxCharSet = BOX_CHARS_DBL;
				break;
			case SINGLE:
				boxCharSet = BOX_CHARS_SGL;
				break;
		}
	}

}

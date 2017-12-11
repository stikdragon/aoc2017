package uk.co.stikman.aoc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * This is an infinite grid
 * </p>
 * <p>
 * This class is <b>not</b> thread safe
 * </p>
 * 
 * @author Stik
 *
 * @param <T>
 */
public class Grid<T> {
	public static final class Key {
		int	x;
		int	y;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Key [x=" + x + ", y=" + y + "]";
		}

		public Key(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Key() {
		}

	}

	private Map<Key, T>			cells	= new HashMap<>();
	private int					minX	= 0;
	private int					maxX	= 0;
	private int					minY	= 0;
	private int					maxY	= 0;
	private static final Key	tmp		= new Key();

	public T get(int x, int y) {
		tmp.x = x;
		tmp.y = y;
		return cells.get(tmp);
	}

	public void put(int x, int y, T t) {
		cells.put(new Key(x, y), t);
		if (x < minX)
			minX = x;
		if (x > maxX)
			maxX = x;
		if (y < minY)
			minY = y;
		if (y > maxY)
			maxY = y;
	}

	public void clear() {
		cells.clear();
		minX = 0;
		minY = 0;
		maxX = 0;
		maxY = 0;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public String render(int cellwidth) {
		TextPainter tp = new TextPainter();
		for (int y = minY; y <= maxY; ++y) {
			for (int x = minX; x <= maxX; ++x) {
				T t = get(x, y);
				String s = "";
				if (t != null)
					s = t.toString();
				if (s.length() > cellwidth)
					s = s.substring(0, cellwidth);
				tp.put((x - minX) * cellwidth, y - minY, s);
			}
		}
		return tp.toString();
	}
}

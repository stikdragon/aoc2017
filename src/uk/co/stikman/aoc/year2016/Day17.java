package uk.co.stikman.aoc.year2016;

import java.util.Arrays;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.TextPainter;
import uk.co.stikman.aoc.utils.Util;

public class Day17 extends AoCBase {
	public static void main(String[] args) {
		new Day17().run(SourceData.get(17), 0, new ConsoleOutput());
		new Day17().run(SourceData.get(17), 1, new ConsoleOutput());
	}

	//
	// Doors are addressed by their X,Y coordinate and a number 0-3:
	//   0: South (+y dir)
	//   1: East (+x dir)
	//   2: North (-y dir)
	//   3: West (-x dir)
	//
	private static final class State {
		private boolean[]	doors	= new boolean[32];	//  doors
		private int			x;
		private int			y;
		private String		path	= "";

		public State() {

		}

		public State(State copy) {
			for (int i = 0; i < 16; ++i)
				doors[i] = copy.doors[i];
		}

		public State(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(doors);
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
			State other = (State) obj;
			if (!Arrays.equals(doors, other.doors))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		/**
		 * if a bit is set then that door is open
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		public int getDoorStates(int x, int y) {
			int r = 0;
			if (y > 0)
				r |= doors[y * 4 + x + 16] ? 1 : 0; // s
			if (x < 3)
				r |= doors[y * 4 + x + 1] ? 2 : 0; // e
			if (y < 3)
				r |= doors[(y + 1) * 4 + x + 16] ? 4 : 0; // n
			if (x > 0)
				r |= doors[y * 4 + x] ? 8 : 0; // w
			return r;
		}

		public void setDoor(int x, int y, int dir, boolean open) {
			switch (dir) {
				case 0: // S
					if (y > 0)
						doors[y * 4 + x + 16] = open;
					break;

				case 1: // E
					if (x < 3)
						setDoor(x + 1, y, 3, open);
					break;
				case 2: // N
					if (y < 3)
						setDoor(x, y + 1, 0, open);
					break;
				case 3: // W
					if (x > 0)
						doors[y * 4 + x] = open;
					break;
			}
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public void assignDoors(int x, int y, char[] arr) {
			setDoor(x, y, 2, testChar(arr[0]));
			setDoor(x, y, 0, testChar(arr[1]));
			setDoor(x, y, 3, testChar(arr[2]));
			setDoor(x, y, 1, testChar(arr[3]));
		}

		private static final boolean testChar(char c) {
			if (c >= 0 && c <= '9' || c == 'a')
				return false;
			return true;
		}

		public int depth() {
			return path.length();
		}

		@Override
		public String toString() {
			return "State [x=" + x + ", y=" + y + ", path=" + path + ", doors=" + Arrays.toString(doors) + "]";
		}

		public String draw() {
			TextPainter tp = new TextPainter();
			for (int y = 0; y < 4; ++y) {
				for (int x = 0; x < 4; ++x) {
					int n = getDoorStates(x, y);
					tp.putChar(x * 2, y * 2, '#');
					tp.putChar(x * 2 + 2, y * 2, '#');
					tp.putChar(x * 2 + 2, y * 2 + 2, '#');
					tp.putChar(x * 2, y * 2 + 2, '#');

					tp.putChar(x * 2 + 1, y * 2 - 0, (n & 0x1) == 0x1 ? ' ' : '-'); // s
					tp.putChar(x * 2 + 1, y * 2 + 2, (n & 0x4) == 0x4 ? ' ' : '-'); // n
					tp.putChar(x * 2 + 2, y * 2 + 1, (n & 0x2) == 0x2 ? ' ' : '|'); // e
					tp.putChar(x * 2 - 0, y * 2 + 1, (n & 0x8) == 0x8 ? ' ' : '|'); // w
				}
			}
			return tp.toString();
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		String passcode = input.trim();
		//
		// My approach is going to be to find any route, then use that
		// as an upper bound and do an exhaustive search of the space 
		// under that, keep repeating until we get there
		//
		final char[] FOUR = new char[4];
		if (part == 0) {
			State start = new State(0, 0);
			System.out.println(start.draw());
			while (true) {
				State state = new State(start);
				String hash = Util.md5(passcode + state.getPath());
				//
				// first 4 chars
				//
				hash.getChars(0, 4, FOUR, 0);
				state.assignDoors(state.x, state.y, FOUR);

			}

		} else if (part == 1) {
		} else
			throw new RuntimeException("Part.. ?");

	}

}

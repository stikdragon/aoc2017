package uk.co.stikman.aoc.year2016;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.RegexHelper;
import uk.co.stikman.aoc.utils.TextPainter;

public class Day8 extends AoCBase {

	public static void main(String[] args) {
		new Day8().run(SourceData.get(8), 0, new ConsoleOutput());
		new Day8().run(SourceData.get(8), 1, new ConsoleOutput());
	}

	private enum Operation {
		/**
		 * Arg3 is 0 for col, 1 for row. Arg1 is position, Arg2 is amount
		 */
		ROTATE,
		/**
		 * Arg1 is X, arg2 is Y, arg3 unused
		 */
		RECT
	}

	private static final class Instruction {
		private Operation					op;
		private int							arg1;
		private int							arg2;
		private int							arg3;
		private static final RegexHelper	reRot	= new RegexHelper("rotate (row y=|column x=)([0-9]+) by ([0-9]+)");

		public Instruction(String text) {
			String[] bits = text.split(" ");
			if (bits[0].equals("rect"))
				op = Operation.RECT;
			else if (bits[0].equals("rotate"))
				op = Operation.ROTATE;
			else
				throw new RuntimeException("Unknown operation: " + bits[0]);

			switch (op) {
				case RECT:
					String[] bits2 = bits[1].split("x");
					if (bits2.length != 2)
						throw new RuntimeException("Rect has invalid size: " + bits[1]);
					arg1 = Integer.parseInt(bits2[0]);
					arg2 = Integer.parseInt(bits2[1]);
					arg3 = 0;
					break;
				case ROTATE:
					List<String> grps = reRot.exec(text);
					if (grps == null)
						throw new RuntimeException("Line " + text + " doesn't parse for ROTATE");
					arg3 = grps.get(0).startsWith("column") ? 0 : 1;
					arg1 = Integer.parseInt(grps.get(1));
					arg2 = Integer.parseInt(grps.get(2));
					break;
				default:
					break;

			}
		}

		@Override
		public String toString() {
			return "Instruction [op=" + op + ", arg1=" + arg1 + ", arg2=" + arg2 + ", arg3=" + arg3 + "]";
		}

		public Operation getOp() {
			return op;
		}

		public int getArg1() {
			return arg1;
		}

		public int getArg2() {
			return arg2;
		}

		public int getArg3() {
			return arg3;
		}
	}

	private static final class Screen {
		private boolean[]	buffer;
		private int			height;
		private int			width;

		public Screen(int w, int h) {
			buffer = new boolean[h * w];
			this.width = w;
			this.height = h;
		}

		public boolean get(int x, int y) {
			return buffer[y * width + x];
		}

		public void set(int x, int y, boolean state) {
			buffer[y * width + x] = state;
		}

		@Override
		public String toString() {
			TextPainter tp = new TextPainter();
			int ptr = 0;
			for (int y = 0; y < height; ++y)
				for (int x = 0; x < width; ++x)
					tp.putChar(x, y, buffer[ptr++] ? '#' : ' ');
			return tp.toString();
		}

		public int getHeight() {
			return height;
		}

		public int getWidth() {
			return width;
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		List<Instruction> lines = Arrays.stream(input.split("\n")).map(String::trim).map(Instruction::new).collect(Collectors.toList());
		if (part == 0) {
			Screen screen = new Screen(50, 6);
			//			Screen screen = new Screen(7, 3);

			for (Instruction inst : lines) {
				switch (inst.getOp()) {
					case RECT:
						for (int x = 0; x < inst.getArg1(); ++x)
							for (int y = 0; y < inst.getArg2(); ++y)
								screen.set(x, y, true);
						break;
					case ROTATE:
						if (inst.getArg3() == 0) { // col
							int pos = inst.getArg1();
							for (int iter = 0; iter < inst.getArg2(); ++iter) {
								boolean tmp = screen.get(pos, screen.height - 1);
								for (int i = screen.getHeight() - 2; i >= 0; --i)
									screen.set(pos, i + 1, screen.get(pos, i));
								screen.set(pos, 0, tmp);
							}
						} else if (inst.getArg3() == 1) { // row
							int pos = inst.getArg1();
							for (int iter = 0; iter < inst.getArg2(); ++iter) {
								boolean tmp = screen.get(screen.width - 1, pos);
								for (int i = screen.getWidth() - 2; i >= 0; --i)
									screen.set(i + 1, pos, screen.get(i, pos));
								screen.set(0, pos, tmp);
							}
						}
						break;
					default:
						break;
				}
			}

			out.println(screen.toString());
			
			int count = 0;
			for (int y = 0; y < screen.getHeight(); ++y)
				for (int x = 0; x < screen.getWidth(); ++x)
					if (screen.get(x, y))
						++count;
			
			out.println("There are " + count + " lit pixels");
			

		} else if (part == 1) {
			out.println("Really cba making this detect the letters, so you'll have to do it by eye!");
			// EOARGPHYAO
		} else
			throw new RuntimeException("Part.. ?");

	}

}

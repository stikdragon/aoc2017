package uk.co.stikman.aoc.year2016;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.RegexHelper;

public class Day15 extends AoCBase {
	public static void main(String[] args) {
		new Day15().run(SourceData.get(15), 0, new ConsoleOutput());
		new Day15().run(SourceData.get(15), 1, new ConsoleOutput());
	}

	private static final RegexHelper reConfig = new RegexHelper("^Disc #([0-9]+) has ([0-9]+) positions; at time=0, it is at position ([0-9]+)\\.$");

	private static final class Disc {
		private int	id;
		private int	positions;
		private int	startingPosition;

		public Disc(String text) {
			List<String> lst = reConfig.exec(text);
			if (lst == null)
				throw new RuntimeException("Line " + text + " does not parse");
			id = Integer.parseInt(lst.get(0));
			positions = Integer.parseInt(lst.get(1));
			startingPosition = Integer.parseInt(lst.get(2));
		}

		public Disc(int id, int positions, int start) {
			this.id = id;
			this.positions = positions;
			this.startingPosition = start;
		}

		public int getId() {
			return id;
		}

		public int getPositions() {
			return positions;
		}

		@Override
		public String toString() {
			return "Disc [id=" + id + ", positions=" + positions + ", startingPosition=" + startingPosition + "]";
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		List<Disc> discs = Arrays.stream(input.split("\n")).map(String::trim).map(Disc::new).sorted((a, b) -> a.getId() - b.getId()).collect(Collectors.toList());

		if (part == 1)
			discs.add(new Disc(discs.size(), 11, 0));
		discs.forEach(l -> out.println(l.toString()));

		for (int buttontime = 0; buttontime < Integer.MAX_VALUE; ++buttontime) {
			boolean b = true;
			for (int i = 1; i <= discs.size(); ++i) {
				Disc d = discs.get(i - 1);
				int pos = (d.startingPosition + buttontime + i) % d.positions;
				if (pos != 0) {
					b = false;
					break;
				}
			}
			if (b) {
				out.println("First time when it'd fall through: " + buttontime);
				return;
			}

		}

	}

}

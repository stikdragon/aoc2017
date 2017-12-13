package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day13 extends AoCBase {

	public static void main(String[] args) {
		new Day13().run(SourceData.get(13), 0, new ConsoleOutput());
		new Day13().run(SourceData.get(13), 1, new ConsoleOutput());
	}

	private static class Layer {
		private int	index;
		private int	range;
		private int	position;
		private int	dir	= 1;

		public void advance() {
			if (dir > 0) {
				++position;
				if (position >= range) {
					dir = -1;
					position -= 2;
				}
			} else {
				--position;
				if (position < 0) {
					dir = 1;
					position += 2;
				}
			}
		}

		public int severity() {
			return index * range;
		}

		public boolean isTop() {
			return position == 0;
		}

		public static Layer parse(String s) {
			String[] bits = s.split(": ?");
			Layer l = new Layer();
			l.index = Integer.parseInt(bits[0]);
			l.range = Integer.parseInt(bits[1]);
			return l;
		}

		public void reset() {
			position = 0;
		}

		@Override
		public String toString() {
			return "Layer [index=" + index + ", range=" + range + ", position=" + position + "]";
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		List<Layer> lines = Arrays.stream(input.split("\n")).map(String::trim).map(Layer::parse).collect(Collectors.toList());

		int max = lines.stream().mapToInt(x -> x.index).max().orElse(0);
		final Layer[] layers = new Layer[max + 1];
		lines.forEach(l -> layers[l.index] = l);

		if (part == 0) {
			int sev = 0;
			for (int pos = 0; pos <= max; ++pos) {
				//
				// we move, then the layers
				//
				if (layers[pos] != null && layers[pos].isTop())
					sev += layers[pos].severity();
				lines.forEach(Layer::advance);
			}

			out.println("Part 1: severity = " + sev);
		} else if (part == 1) {
			int delay = 0;
			for (;;) {
				lines.forEach(Layer::reset);
				for (int i = 0; i < delay; ++i)
					lines.forEach(Layer::advance);

				int sev = 0;
				for (int pos = 0; pos <= max; ++pos) {
					//
					// we move, then the layers
					//
					if (layers[pos] != null && layers[pos].isTop())
						sev += layers[pos].severity();
					lines.forEach(Layer::advance);
				}
				if (sev == 0) {
					out.println("Part 2: delay needs to be " + delay + " to avoid all");
					return;
				}
					
				++delay;
			}
		}

	}

}

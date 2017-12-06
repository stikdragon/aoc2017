package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day6 extends AoCBase {

	public static void main(String[] args) {
		new Day6().run(SourceData.get(6), 0, new ConsoleOutput());
		new Day6().run(SourceData.get(6), 1, new ConsoleOutput());
	}

	private static final class Blocks {
		private List<Integer> blocks = new ArrayList<>();

		public Blocks(List<Integer> copy) {
			blocks.addAll(copy);
		}

		public List<Integer> getBlocks() {
			return blocks;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((blocks == null) ? 0 : blocks.hashCode());
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
			Blocks other = (Blocks) obj;
			if (blocks == null) {
				if (other.blocks != null)
					return false;
			} else if (!blocks.equals(other.blocks))
				return false;
			return true;
		}

		public Blocks redistribute() {
			//
			// find biggest
			//
			Blocks b = new Blocks(this.blocks);
			int best = b.blocks.get(0);
			int bestI = 0;
			for (int i = 1; i < b.blocks.size(); ++i) {
				if (b.blocks.get(i) > best) {
					bestI = i;
					best = b.blocks.get(i);
				}
			}

			//
			// distribute
			//
			int idx = bestI + 1;
			b.blocks.set(bestI, 0);
			while (best > 0) {
				if (idx >= b.blocks.size())
					idx = 0;
				b.blocks.set(idx, b.blocks.get(idx) + 1);
				--best;
				++idx;
			}
			return b;
		}

		@Override
		public String toString() {
			return blocks.toString();
		}

	}

	@Override
	public void run(String input, int part, Output output) {
		List<Integer> blocks = Arrays.stream(input.split("\t")).map(Integer::parseInt).collect(Collectors.toList());
		Blocks cur = new Blocks(blocks);
		Map<Blocks, Integer> seen = new HashMap<>();
		seen.put(cur, 0);
		int cnt = 1;
		for (;;) {
			cur = cur.redistribute();
			System.out.println(cur);
			Integer last = seen.get(cur);
			if (last != null) {
				output.println("Part2: " + (cnt - last));
				break;
			}
			seen.put(cur, cnt);
			++cnt;
		}

		output.println("Part " + (part + 1) + " takes " + cnt + " iterations to repeat a state");
	}

}

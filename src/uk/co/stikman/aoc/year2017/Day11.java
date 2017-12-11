package uk.co.stikman.aoc.year2017;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.maths.Vector2;
import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day11 extends AoCBase {
	
	public static void main(String[] args) {
		new Day11().run(SourceData.get(11), 0, new ConsoleOutput());
		new Day11().run(SourceData.get(11), 1, new ConsoleOutput());
	}

	//
	// Gah, so i started doing A* on this before realising how much simpler
	// it was
	//
	
	@Override
	public void run(String input, int part, Output out) {
		List<String> moves = Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());

		Map<String, Vector2> dirs = new HashMap<>();
		dirs.put("n", new Vector2(0, -1));
		dirs.put("ne", new Vector2(1, -1));
		dirs.put("se", new Vector2(1, 0));
		dirs.put("s", new Vector2(0, 1));
		dirs.put("sw", new Vector2(-1, 1));
		dirs.put("nw", new Vector2(-1, 0));

		Vector2 start = Vector2.ZERO;
		Vector2 cur = new Vector2();
		int bestN = 0;
		for (String move : moves) {
			Vector2 dir = dirs.get(move);
			cur.addLocal(dir);
			if (stepsBetween(start, cur) > bestN)
				bestN = stepsBetween(start, cur);
		}

		Vector2 finish = cur;
		int steps = stepsBetween(start, finish);

		if (part == 0) {
			out.println("Part 1: Position = " + cur);
			out.println("Part 1: Steps = " + steps);
		}
		if (part == 1) 
			out.println("Part 2: greatest distance is: " + bestN);

	}

	private int stepsBetween(Vector2 start, Vector2 finish) {
		return (int) (Math.abs(finish.y - start.y) + Math.abs(finish.x - start.x));
	}

}

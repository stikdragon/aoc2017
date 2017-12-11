package uk.co.stikman.aoc.year2017;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.maths.Matrix3;
import uk.co.stikman.aoc.maths.Vector2;
import uk.co.stikman.aoc.utils.AStar;
import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Coord;
import uk.co.stikman.aoc.utils.Output;

public class Day11 extends AoCBase {

	public static void main(String[] args) {
		new Day11().run(SourceData.get(11), 0, new ConsoleOutput());
		new Day11().run(SourceData.get(11), 1, new ConsoleOutput());
	}

	@Override
	public void run(String input, int part, Output out) {
		List<String> moves = Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());

		Map<String, Vector2> dirs = new HashMap<>();
		dirs.put("ne", new Vector2(1, -1));
		dirs.put("se", new Vector2(1, 1));
		dirs.put("s", new Vector2(0, 1));
		dirs.put("sw", new Vector2(-1, 1));
		dirs.put("nw", new Vector2(-1, 1));
		dirs.put("n", new Vector2(0, -1));

		Vector2 cur = new Vector2();
		for (String move : moves) {
			Vector2 dir = dirs.get(move);
			cur.addLocal(dir);
		}
		
		final Matrix3 skew = new Matrix3();
		skew.skew(1.0f, 1.5f);
		final Matrix3 iskew = skew.inverse();
		final Vector2 v1 = new Vector2();
		final Vector2 v2 = new Vector2();
		final Vector2 v3 = new Vector2();
		final Vector2 v4 = new Vector2();
		//
		// Do search to find route
		//
		AStar<Vector2> star = new AStar<Vector2>() {
			@Override
			protected void getNeighbours(Vector2 cur, List<Vector2> list) {
				for (Vector2 v : dirs.values())
					list.add(cur.add(v, new Vector2()));
			}

			@Override
			protected int dist(Vector2 from, Vector2 to) {
				skew.multiply(from, v1);
				skew.multiply(to, v2);
				return (int) v1.distanceTo(v2);
			}

			@Override
			protected int costEstimate(Vector2 from, Vector2 to) {
				skew.multiply(from, v1);
				skew.multiply(to, v2);
				return (int) v1.distanceTo(v2);
			}
		};
		

		out.println("Part 1: Position = " + cur);
		List<Vector2> path = star.execute(Vector2.ZERO, cur);
		out.println("Part 1: parth length = " + path.size());

	}

	private int hexDist(Vector2 start, Vector2 end) {
		return 0;
	}

}

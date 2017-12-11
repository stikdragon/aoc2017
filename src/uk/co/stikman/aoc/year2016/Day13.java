package uk.co.stikman.aoc.year2016;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Coord;
import uk.co.stikman.aoc.utils.IntMap;
import uk.co.stikman.aoc.utils.Output;

public class Day13 extends AoCBase {

	public static void main(String[] args) {
		new Day13().run(SourceData.get(13), 0, new ConsoleOutput());
		new Day13().run(SourceData.get(13), 1, new ConsoleOutput());
	}

	private static Coord[] neighbours = new Coord[4];
	static {
		neighbours[0] = new Coord(-1, 0);
		neighbours[1] = new Coord(1, 0);
		neighbours[2] = new Coord(0, -1);
		neighbours[3] = new Coord(0, 1);
	}

	@Override
	public void run(String input, int part, Output out) {
		int seed = Integer.parseInt(input);
		//		seed = 10; // sample
		//		if (part == 0)
		//			drawSample(seed, out, null);

		if (part == 0) {
			//
			// bog standard a* search
			//
			Coord start = new Coord(1, 1);
			//			Coord destination = new Coord(7, 4);
			Coord destination = new Coord(31, 39);

			Set<Coord> open = new HashSet<>();
			Set<Coord> closed = new HashSet<>();
			Map<Coord, Coord> from = new HashMap<>();
			IntMap<Coord> gscores = new IntMap<>(Integer.MAX_VALUE / 2);
			gscores.put(start, 0);
			IntMap<Coord> fscores = new IntMap<>(Integer.MAX_VALUE / 2);
			fscores.put(start, costEstimate(start, destination));

			List<Coord> path = null;
			open.add(new Coord(1, 1));
			while (!open.isEmpty()) {
				Iterator<Coord> iter = open.iterator();
				Coord cur = iter.next();
				iter.remove();

				if (cur.equals(destination)) {
					path = constructPath(from, cur);
					break;
				}

				closed.add(cur);

				for (int n = 0; n < neighbours.length; ++n) {
					Coord offset = neighbours[n];
					Coord neigh = new Coord(cur.x + offset.x, cur.y + offset.y);
					if (closed.contains(neigh))
						continue;
					if (!isWall(neigh.x, neigh.y, seed) && !open.contains(neigh))
						open.add(neigh);
					int tent = gscores.get(cur) + dist(cur, neigh);
					if (tent >= gscores.get(neigh))
						continue;
					from.put(neigh, cur);
					gscores.put(neigh, tent);
					fscores.put(neigh, tent + costEstimate(neigh, destination));
				}
			}

			out.println(path.toString());
			drawSample(seed, "out\\Day13_1.png", out, path);
			out.println("Part 1: That's " + (path.size() - 1) + " steps");
		} else if (part == 1) {
			//
			// Recursive flood fill 
			//
			Coord start = new Coord(1, 1);
			IntMap<Coord> visited = new IntMap<>(0);
			floodFill(seed, start, 1, 50, visited);

			drawSample(seed, "out\\Day13_2.png", out, visited.coordList());

			out.println("Part 2: There's " + visited.size() + " locations you could get to");

		} else
			throw new RuntimeException("Part.. ?");

	}

	private void floodFill(int seed, Coord current, int depth, int maxdepth, IntMap visited) {
		if (isWall(current.x, current.y, seed))
			return;
		int existing = visited.get(current);
		if (existing < depth) // we've alreayd been here, but we had a smaller stack that time, so let that one run it instead
			return;
		visited.put(current, depth);
		if (depth > maxdepth)
			return;
		for (int n = 0; n < neighbours.length; ++n) {
			Coord offset = neighbours[n];
			Coord v = new Coord(offset.x + current.x, offset.y + current.y);
			floodFill(seed, v, depth + 1, maxdepth, visited);
		}
	}

	private int costEstimate(Coord from, Coord to) {
		int dx = from.x - to.x;
		int dy = from.y - to.y;
		return dx * dx + dy * dy;
	}

	private int dist(Coord from, Coord to) {
		int dx = from.x - to.x;
		int dy = from.y - to.y;
		return Math.abs(dx) + Math.abs(dy);
	}

	private List<Coord> constructPath(Map<Coord, Coord> from, Coord cur) {
		List<Coord> res = new ArrayList<>();
		for (;;) {
			res.add(cur);
			cur = from.get(cur);
			if (cur == null)
				break;
		}
		return res;
	}

	public void drawSample(int seed, String name, Output out, Collection<Coord> path) {
		int maxX = path == null ? 10 : (path.stream().mapToInt(c -> c.x).max().orElse(0) + 5);
		int maxY = path == null ? 7 : (path.stream().mapToInt(c -> c.y).max().orElse(0) + 5);
		if (maxX > 2000)
			maxX = 2000; // apply some sense
		if (maxY > 2000)
			maxY = 2000;

		BufferedImage img = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < maxY; ++y)
			for (int x = 0; x < maxX; ++x)
				img.setRGB(x, y, isWall(x, y, seed) ? 0x0 : 0xffffff);

		if (path != null)
			for (Coord c : path)
				if (c.x <= maxX && c.y <= maxY)
					img.setRGB(c.x, c.y, 0xff);
		File f = new File(name);
		try {
			ImageIO.write(img, "png", f);
		} catch (IOException e) {
			out.println("ERROR: Couldn't save file " + f.getAbsolutePath());
		}
		out.println("Saved file to " + f.getAbsolutePath());

	}

	private static final boolean isWall(int x, int y, int seed) {
		if (x == 1 && y == 1) // starting condition
			return false;
		if (x < 0 || y < 0)
			return true;
		int n = x * x + 3 * x + 2 * x * y + y + y * y + seed;
		int c;
		for (c = 0; n > 0; c++)
			n &= n - 1;
		return c % 2 == 1;
	}

}

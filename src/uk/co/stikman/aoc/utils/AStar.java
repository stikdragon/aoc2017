package uk.co.stikman.aoc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A very simple implementation of the A* algorithm
 *
 * @param <T>
 */
public abstract class AStar<T> {
	/**
	 * 
	 * Returns <code>null</code> if no path can be found
	 * 
	 * @param start
	 * @param destination
	 * @return
	 */
	public List<T> execute(T start, T destination) {

		final FloatMap<T> gscores = new FloatMap<>(Integer.MAX_VALUE);
		final FloatMap<T> fscores = new FloatMap<>(Integer.MAX_VALUE);
		SortableSet<T> open = new SortableSet<T>((a, b) -> {
			return Float.compare(fscores.get(a), fscores.get(b));
		});
		Set<T> closed = new HashSet<>();
		Map<T, T> from = new HashMap<>();
		gscores.put(start, 0);
		fscores.put(start, costEstimate(start, destination));


		List<T> tmp = new ArrayList<>();
		open.add(start);
		while (!open.isEmpty()) {
			T cur = open.removeFirst();

			if (cur.equals(destination)) 
				return constructPath(from, cur);

			closed.add(cur);

			tmp.clear();
			getNeighbours(cur, tmp);

			for (T neigh : tmp) {
				if (closed.contains(neigh))
					continue;
				if (!open.contains(neigh))
					open.add(neigh);
				float tent = gscores.get(cur) + dist(cur, neigh);
				if (tent >= gscores.get(neigh))
					continue;
				from.put(neigh, cur);
				gscores.put(neigh, tent);
				fscores.put(neigh, tent + costEstimate(neigh, destination));
			}
		}

		return null;
	}

	protected abstract void getNeighbours(T cur, List<T> list);

	protected abstract float dist(T from, T to);

	protected abstract float costEstimate(T from, T to);

	private List<T> constructPath(Map<T, T> from, T cur) {
		List<T> res = new ArrayList<>();
		for (;;) {
			res.add(cur);
			cur = from.get(cur);
			if (cur == null)
				break;
		}
		return res;
	}

}

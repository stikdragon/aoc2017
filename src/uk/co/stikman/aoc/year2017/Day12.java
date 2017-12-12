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

public class Day12 extends AoCBase {

	public static void main(String[] args) {
		new Day12().run(SourceData.get(12), 0, new ConsoleOutput());
		new Day12().run(SourceData.get(12), 1, new ConsoleOutput());
	}

	private static class Program {
		private String			name;
		private int				group;
		private List<Program>	links	= new ArrayList<>();

		public Program(String name) {
			super();
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Program other = (Program) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		if (part == 1)
			return;
		List<String> lines = Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());

		Map<String, Program> lkp = new HashMap<>();
		for (String line : lines) {
			String[] bits = line.split(" ?<-> ?");
			String[] list = bits[1].split(", ?");
			Program p = lkp.get(bits[0]);
			if (p == null)
				lkp.put(bits[0], p = new Program(bits[0]));
			for (String lnk : list) {
				Program q = lkp.get(lnk.trim());
				if (q == null)
					lkp.put(lnk.trim(), q = new Program(lnk.trim()));
				q.links.add(p);
				p.links.add(q);
			}
		}

		Set<Program> pending = new HashSet<>();
		Set<Program> remain = new HashSet<>();
		remain.addAll(lkp.values());
		int group = 1;
		while (!remain.isEmpty()) {
			Program p = remain.iterator().next();
			remain.remove(p);

			pending.add(p);
			while (!pending.isEmpty()) {
				Program cur = pending.iterator().next();
				pending.remove(cur);
				remain.remove(cur);
				cur.group = group;
				for (Program lnk : cur.links) {
					if (lnk.group != 0)
						continue; // already been here
					pending.add(lnk);
				}
			}
			++group;
		}

		//
		// find which group number 0 is in, and count the rest
		//
		Program p = lkp.get("0");
		int cnt = (int) lkp.values().stream().filter(a -> a.group == p.group).count();
		out.println("Part 1: There's " + cnt + " programs in group " + p.group);
		out.println("Part 2: There's " + group + " groups");
	}

}

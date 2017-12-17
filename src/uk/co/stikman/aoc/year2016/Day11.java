package uk.co.stikman.aoc.year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day11 extends AoCBase {
	public static void main(String[] args) {
		new Day11().run(SourceData.get(11), 0, new ConsoleOutput());
		new Day11().run(SourceData.get(11), 1, new ConsoleOutput());
	}

	/**
	 * 
	 * <pre>
	4    -    -    -    -    -    -    -    -    -    -  
	3    -    -    -    -    -    -    -    -    -    -
	2    -   PoM   -   PrM   -    -    -    -    -    -  
	1   PoG   -   PrG   -   ThG  ThM  RuG  RuM  CoG  CoM
	 * </pre>
	 * 
	 **/

	private static class Floor {
		private int				num;
		private List<String>	items	= new ArrayList<>();

		public Floor(int num) {
			this.num = num;
		}

		@Override
		public String toString() {
			return "Floor [num=" + num + ", items=" + items + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((items == null) ? 0 : items.hashCode());
			result = prime * result + num;
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
			Floor other = (Floor) obj;
			if (items == null) {
				if (other.items != null)
					return false;
			} else if (!items.equals(other.items))
				return false;
			if (num != other.num)
				return false;
			return true;
		}

		public Floor copy() {
			Floor res = new Floor(num);
			res.items.addAll(items);
			return res;
		}

	}

	private static class Configuration {
		private int		elevator	= 0;
		private Floor[]	floors		= new Floor[4];

		public Configuration() {
			for (int i = 0; i < floors.length; ++i)
				floors[i] = new Floor(i);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Elevator is on floor: " + (elevator + 1)).append("\n");
			for (Floor f : floors)
				sb.append(f.toString()).append("\n");
			return sb.toString();
		}

		public Configuration copy() {
			Configuration res = new Configuration();
			res.elevator = this.elevator;
			for (int i = 0; i < floors.length; ++i)
				res.floors[i] = floors[i].copy();
			return res;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + elevator;
			result = prime * result + Arrays.hashCode(floors);
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
			Configuration other = (Configuration) obj;
			if (elevator != other.elevator)
				return false;
			if (!Arrays.equals(floors, other.floors))
				return false;
			return true;
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		Configuration config = parseConfiguration(input);
		if (part == 0) {
			out.println(config.toString());

			Set<Configuration> seen = new HashSet<>();
			LinkedList<Configuration> pending = new LinkedList<>();
			pending.add(config);

			while (!pending.isEmpty()) {
				Configuration current = pending.removeFirst();
				if (!seen.add(current)) // been here before 
					continue;

				//
				// Work out possible moves we can make with the current one
				//
				
				
			}

		} else if (part == 1) {

		} else
			throw new RuntimeException("Part.. ?");

	}

	private Configuration parseConfiguration(String input) {
		Configuration c = new Configuration();
		for (String line : input.split("\n")) {
			String[] bits = line.split(" ");
			int n = convertWordNumber(bits[1]);
			String a = Arrays.stream(bits).skip(4).collect(Collectors.joining(" "));
			if (a.equals("nothing relevant."))
				continue;
			for (String s : a.split(",")) {
				s = s.trim();
				s = s.replace("-compatible", "");
				s = s.replace(".", "");
				s = s.replace("a ", "");
				s = s.replace("and ", "");
				bits = s.split(" ");
				c.floors[n - 1].items.add(bits[0] + "-" + bits[1]);
			}

		}
		return c;
	}

	private int convertWordNumber(String s) {
		if ("first".equalsIgnoreCase(s))
			return 1;
		if ("second".equalsIgnoreCase(s))
			return 2;
		if ("third".equalsIgnoreCase(s))
			return 3;
		if ("fourth".equalsIgnoreCase(s))
			return 4;
		throw new RuntimeException("Unknown floor number: " + s);
	}

}

package uk.co.stikman.aoc.year2016;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day4 extends AoCBase {
	public static void main(String[] args) {
		new Day4().run(SourceData.get(4), 0, new ConsoleOutput());
		new Day4().run(SourceData.get(4), 1, new ConsoleOutput());
	}

	private static final class Room {
		private String	name;
		private int		sector;
		private String	checksum;

		public static Room parse(String s) {
			Room r = new Room();
			int pos = s.lastIndexOf('-');
			if (pos == -1)
				throw new RuntimeException("Invalid room string: " + s);
			r.name = s.substring(0, pos);
			String rest = s.substring(pos + 1);
			if (!rest.matches("^[0-9]+\\[[a-z]+\\]$"))
				throw new RuntimeException("Invalid room string: " + s);
			pos = rest.indexOf('[');
			r.sector = Integer.parseInt(rest.substring(0, pos));
			r.checksum = rest.substring(pos + 1, rest.length() - 1);
			return r;
		}

		@Override
		public String toString() {
			return "Room [name=" + name + ", sector=" + sector + ", checksum=" + checksum + "]";
		}

		/**
		 * Test if checksum valid
		 * 
		 * @return
		 */
		public boolean valid() {
			Map<Character, Integer> hist = new HashMap<>();
			for (char ch : name.toCharArray()) {
				Integer i = hist.get(ch);
				if (i == null)
					hist.put(ch, 1);
				else
					hist.put(ch, i + 1);
			}
			hist.remove('-');

			//
			// sort entries
			//
			List<Character> histlist = hist.entrySet().stream().sorted((a, b) -> {
				int n = b.getValue() - a.getValue();
				if (n == 0)
					return a.getKey().compareTo(b.getKey());
				return n;
			}).map(e -> e.getKey()).collect(Collectors.toList());
			int i = 0;
			for (char ch : checksum.toCharArray()) {
				if (ch != histlist.get(i++))
					return false;
			}
			return true;
		}

		public void decrypt() {
			char[] arr = name.toCharArray();
			for (int i = 0; i < arr.length; ++i) {
				int n = (((arr[i] - 'a') + sector) % 26) + 'a';
				arr[i] = (char) n;
			}
			name = new String(arr);
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		List<Room> list = Arrays.stream(input.split("\n")).map(String::trim).map(Room::parse).collect(Collectors.toList());
		if (part == 0) {
			removeDecoys(list);
			int sum = 0;
			for (Room r : list)
				sum += r.sector;
			out.println("Sum of sectors: " + sum);
		} else if (part == 1) {
			removeDecoys(list);
			list.forEach(r -> r.decrypt());
			list.forEach(r -> out.println(r.toString()));
			for (Room r : list) {
				if (r.name.contains("northpole") && r.name.contains("object") && r.name.contains("storage")) {
					out.println("North pole objects at: " + r.sector);
					out.println("Stopping.");
					return;
				}
			}
		} else
			throw new RuntimeException("Part.. ?");

	}

	private void removeDecoys(List<Room> list) {
		Iterator<Room> iter = list.iterator();
		while (iter.hasNext()) {
			Room r = iter.next();
			if (!r.valid())
				iter.remove();
		}

	}

}

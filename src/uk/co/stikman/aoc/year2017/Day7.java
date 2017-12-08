package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;

public class Day7 extends AoCBase {

	public static void main(String[] args) {
		new Day7().run(SourceData.get(7), 0, new ConsoleOutput());
		new Day7().run(SourceData.get(7), 1, new ConsoleOutput());
	}

	private static final class Prog {
		private List<Prog>		children	= new ArrayList<>();
		private final int		weight;
		private final String	name;
		private Prog			parent;

		public Prog(String name, int weight) {
			this.weight = weight;
			this.name = name;
		}

		public List<Prog> getChildren() {
			return children;
		}

		public int getWeight() {
			return weight;
		}

		public Prog getParent() {
			return parent;
		}

		public void addChild(Prog p) {
			children.add(p);
			p.parent = this;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "Prog [name=" + name + ", weight=" + weight + ", parent=" + parent + "]";
		}

		public int calcTotalWeight() {
			int sum = weight;
			for (Prog c :children)
				sum += c.calcTotalWeight();
			return sum;
		}

	}

	@Override
	public void run(String input, int part, Output output) {
		List<String> lines = Arrays.stream(input.split("\n")).map(String::trim).collect(Collectors.toList());
		Map<String, Prog> progs = new HashMap<>();
		Map<Prog, List<String>> childrens = new HashMap<>();
		for (String line : lines) {
			String[] bits = line.split(" ");
			Prog p = new Prog(bits[0], Integer.parseInt(bits[1].replaceAll("[()]", "")));
			progs.put(p.getName(), p);
			if (bits.length > 2) {
				List<String> lst = new ArrayList<>();
				for (int i = 3; i < bits.length; ++i)
					lst.add(bits[i].replaceAll(",", ""));
				childrens.put(p, lst);
			}
		}

		//
		// build tree
		//
		for (Prog p : progs.values()) {
			List<String> children = childrens.get(p);
			if (children != null)
				for (String s : children)
					p.addChild(progs.get(s));
		}

		//
		// Find Prog without a parent
		//
		List<Prog> roots = progs.values().stream().filter(p -> p.getParent() == null).collect(Collectors.toList());
		if (roots.size() != 1)
			throw new RuntimeException("Expected a single root but found " + roots.size());
		if (part == 0)
			output.println("Root node is: " + roots.get(0));


		if (part == 1) {
			print(progs.get("gexwzw"), output, 0, 1);

		}

	}

	private void print(Prog prog, Output output, int depth, int max) {
		if (depth> max)
			return;
		String s = "";
		for (int i = 0; i < depth; ++i)
			s += " ";
		
		int sum = prog.calcTotalWeight();
		
		output.println(s + prog.getName() + " (" + prog.getWeight() + ", sum of children = " + sum + ")");
		for (Prog c : prog.getChildren())
			print(c, output, depth + 1, max);
	}

}

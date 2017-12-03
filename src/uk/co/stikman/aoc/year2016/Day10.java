package uk.co.stikman.aoc.year2016;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.RegexHelper;

public class Day10 extends AoCBase {
	public static void main(String[] args) {
		new Day10().run(SourceData.get(10), 0, new ConsoleOutput());
		new Day10().run(SourceData.get(10), 1, new ConsoleOutput());
	}

	private enum Operation {
		INIT, TRANSFER
	}

	//
	// ther's only two ops so just going to have a single class with shared fields
	//
	private static final class Instruction {

		private static final RegexHelper	reInit		= new RegexHelper("^value ([0-9]+) goes to bot ([0-9]+)$");
		private static final RegexHelper	reTransfer	= new RegexHelper("^bot ([0-9]+) gives low to (output|bot) ([0-9]+) and high to (output|bot) ([0-9]+)$");

		private Operation					operation;
		private int							arg1;
		private boolean						output2;
		private int							arg2;
		private boolean						output3;
		private int							arg3;

		public Instruction(String text) {
			List<String> lst = reInit.exec(text);
			if (lst != null) {
				operation = Operation.INIT;
				arg1 = Integer.parseInt(lst.get(0));
				arg2 = Integer.parseInt(lst.get(1));
			} else {
				lst = reTransfer.exec(text);
				if (lst == null)
					throw new RuntimeException("Invalid instruction line: " + text);
				operation = Operation.TRANSFER;
				arg1 = Integer.parseInt(lst.get(0));
				output2 = lst.get(1).equals("output");
				arg2 = Integer.parseInt(lst.get(2));
				output3 = lst.get(3).equals("output");
				arg3 = Integer.parseInt(lst.get(4));
			}
		}

		@Override
		public String toString() {
			switch (operation) {
				case INIT:
					return "INIT: val " + arg1 + " goes to bot " + arg2;
				case TRANSFER:
					return "GIVE: bot " + arg1 + " gives high to " + (output2 ? "output" : "bot") + " " + arg2 + " and low to " + (output3 ? "output" : "bot") + " " + arg3;
				default:
					return "?";
			}
		}

		public Operation getOperation() {
			return operation;
		}

		public int getArg1() {
			return arg1;
		}

		public boolean isOutput2() {
			return output2;
		}

		public int getArg2() {
			return arg2;
		}

		public boolean isOutput3() {
			return output3;
		}

		public int getArg3() {
			return arg3;
		}
	}

	private static class Bot {
		private final int	id;
		private int			low		= -1;
		private int			high	= -1;
		private Instruction	instruction;

		public Bot(int id) {
			super();
			this.id = id;
		}

		public int getLow() {
			return low;
		}

		public int getHigh() {
			return high;
		}

		public void receive(int value) {
			if (low != -1 && high != -1)
				throw new RuntimeException("Bot already has two values");
			if (low == -1)
				low = value;
			else if (high == -1)
				high = value;
			if (low != -1 && high != -1) {
				if (low > high) {
					int t = low;
					low = high;
					high = t;
				}
			}
		}

		public boolean isReady() {
			return (low != -1 && high != -1);
		}

		public Instruction getInstruction() {
			return instruction;
		}

		public void setInstruction(Instruction instruction) {
			if (this.instruction != null)
				throw new IllegalArgumentException("Bot " + id + " already has an instruction");
			this.instruction = instruction;
		}

		public int getId() {
			return id;
		}

		public void clearValues() {
			low = -1;
			high = -1;
		}
	}

	@Override
	public void run(String input, int part, Output out) {
		List<Instruction> lines = Arrays.stream(input.split("\n")).map(String::trim).map(Instruction::new).collect(Collectors.toList());
		lines.sort((a, b) -> a.operation.compareTo(b.operation));

		for (Instruction inst : lines)
			out.println(inst.toString());

		Map<Integer, Integer> outputs = new HashMap<>();

		//
		// Do init ops first
		//
		Map<Integer, Bot> bots = new HashMap<>();
		for (Instruction inst : lines) {
			if (inst.getOperation() == Operation.INIT)
				getOrCreate(bots, inst.getArg2()).receive(inst.getArg1());
			else if (inst.getOperation() == Operation.TRANSFER)
				getOrCreate(bots, inst.getArg1()).setInstruction(inst);
		}
		//
		// Keep processing bots until there's nothign left to do
		//
		LinkedList<Bot> pending = new LinkedList<>();
		pending.addAll(bots.values());
		while (!pending.isEmpty()) {
			Bot b = pending.removeFirst();
			if (b.isReady()) {
				//
				// Move values to targets
				//
				int lowId = b.getInstruction().getArg2();
				int hiId = b.getInstruction().getArg3();

				//
				// Look for our puzzle solution
				//
				if (part == 0 && b.getHigh() == 61 && b.getLow() == 17)
					out.println("Bot that compares 61 and 17 is number: " + b.getId());

				if (b.getInstruction().isOutput2())
					outputs.put(lowId, b.getLow());
				else
					getOrCreate(bots, lowId).receive(b.getLow());
				pending.addLast(bots.get(lowId));

				if (b.getInstruction().isOutput3())
					outputs.put(hiId, b.getHigh());
				else
					getOrCreate(bots, hiId).receive(b.getHigh());
				pending.addLast(bots.get(hiId));
				b.clearValues();
			}
		}

		outputs.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).forEach(e -> out.println(e.getKey() + ": " + e.getValue()));

		if (part == 1)
			out.println("Product of first three outputs: " + (outputs.get(0) * outputs.get(1) * outputs.get(2)));

	}

	private static Bot getOrCreate(Map<Integer, Bot> bots, int id) {
		Bot b = bots.get(id);
		if (b == null)
			bots.put(id, b = new Bot(id));
		return b;
	}

}

package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.year2017.Day8.Operator;

public class Day8 extends AoCBase {

	public static void main(String[] args) {
		new Day8().run(SourceData.get(8), 0, new ConsoleOutput());
		new Day8().run(SourceData.get(8), 1, new ConsoleOutput());
	}

	private enum Type {
		INC, DEC
	}

	public enum Operator {
		LT("<"), GT(">"), LTE("<="), GTE(">="), EQ("=="), NE("!=");

		private String symbol;

		Operator(String sym) {
			this.symbol = sym;
		}

		public static Operator find(String in) {
			for (Operator o : values())
				if (in.equals(o.symbol))
					return o;
			throw new NoSuchElementException(in);
		}
	}

	private static final class Instruction {
		private Type		type;
		private String		register;
		private String		queryRegister;
		private Operator	op;
		private int			value;
		private int			queryValue;

		public static Instruction parse(String src) {
			String[] bits = src.split(" ");
			if (bits.length != 7)
				throw new RuntimeException("Invalid line: " + src);

			Instruction inst = new Instruction();
			inst.register = bits[0];
			inst.type = bits[1].equals("inc") ? Type.INC : Type.DEC;
			inst.queryRegister = bits[4];
			inst.op = Operator.find(bits[5]);
			inst.value = Integer.parseInt(bits[2]);
			inst.queryValue = Integer.parseInt(bits[6]);
			return inst;
		}

		public Type getType() {
			return type;
		}

		public String getRegister() {
			return register;
		}

		public String getQueryRegister() {
			return queryRegister;
		}

		public Operator getOp() {
			return op;
		}

		public int getValue() {
			return value;
		}

		public int getQueryValue() {
			return queryValue;
		}

		@Override
		public String toString() {
			return register + " " + type + " " + value + " if " + queryRegister + " " + op + " " + queryValue;
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		List<Instruction> instructions = Arrays.stream(input.split("\n")).map(Instruction::parse).collect(Collectors.toList());
		for (Instruction inst : instructions)
			System.out.println(inst);

		long biggestEva = 0;

		Map<String, Long> regs = new HashMap<>();
		for (Instruction inst : instructions) {
			Long l = regs.get(inst.getRegister());
			if (l == null)
				l = 0L;
			Long t = regs.get(inst.getQueryRegister());
			if (t == null)
				t = 0L;
			boolean b = false;
			switch (inst.getOp()) {
				case EQ:
					b = t == inst.getQueryValue();
					break;
				case GT:
					b = t > inst.getQueryValue();
					break;
				case GTE:
					b = t >= inst.getQueryValue();
					break;
				case LT:
					b = t < inst.getQueryValue();
					break;
				case LTE:
					b = t <= inst.getQueryValue();
					break;
				case NE:
					b = t != inst.getQueryValue();
					break;
			}
			if (b) {
				if (inst.getType() == Type.INC)
					l += inst.getValue();
				else
					l -= inst.getValue();
				regs.put(inst.getRegister(), l);
				if (l > biggestEva)
					biggestEva = l;
			}
		}

		String best = null;
		long bestN = 0;
		for (Entry<String, Long> e : regs.entrySet()) {
			if (e.getValue() > bestN) {
				bestN = e.getValue();
				best = e.getKey();
			}
		}

		if (part == 0)
			out.println("Biggest register is " + best + " == " + bestN);
		if (part == 1)
			out.println("Biggest at any time is " + biggestEva);

	}

}

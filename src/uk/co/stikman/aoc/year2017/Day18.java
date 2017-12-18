package uk.co.stikman.aoc.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import uk.co.stikman.aoc.utils.AoCBase;
import uk.co.stikman.aoc.utils.ConsoleOutput;
import uk.co.stikman.aoc.utils.Output;
import uk.co.stikman.aoc.utils.Util;

public class Day18 extends AoCBase {

	public static void main(String[] args) {
		new Day18().run(SourceData.get(18), 0, new ConsoleOutput());
		new Day18().run(SourceData.get(18), 1, new ConsoleOutput());
	}

	public interface Target {
		long get(Context ctx);

		void put(Context ctx, long v);
	}

	public static class TargetInt implements Target {
		private long val;

		public TargetInt(long val) {
			this.val = val;
		}

		@Override
		public long get(Context ctx) {
			return val;
		}

		@Override
		public String toString() {
			return Long.toString(val);
		}

		@Override
		public void put(Context ctx, long v) {
			this.val = v;
		}
	}

	public static class TargetReg implements Target {
		private char name;

		public TargetReg(char name) {
			this.name = name;
		}

		@Override
		public long get(Context ctx) {
			return ctx.getReg(name);
		}

		@Override
		public void put(Context ctx, long v) {
			ctx.putReg(name, v);
		}

		@Override
		public String toString() {
			return "" + name;
		}
	}

	public static class Context {
		public int						ptr;
		public Map<Character, Long>		registers	= new HashMap<>();
		public LinkedList<Long>			queue		= new LinkedList<>();
		public Context					sendTo;
		public final List<Instruction>	code;
		public boolean					needInput	= false;
		public final String				name;
		public int						sendCounter	= 0;

		public Context(String name, List<Instruction> code) {
			super();
			this.name = name;
			this.code = code;
		}

		public boolean canRun() {
			if (ptr < 0 || ptr >= code.size())
				return false;
			if (needInput && queue.isEmpty())
				return false;
			return true;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			String sep = "";
			for (Entry<Character, Long> e : registers.entrySet()) {
				sb.append(sep).append(e.getKey()).append(":").append(e.getValue());
				sep = ", ";
			}
			return sb.toString();
		}

		public void putReg(char name, long val) {
			registers.put(name, val);
		}

		public long getReg(char name) {
			Long x = registers.get(name);
			if (x == null)
				return 0;
			return x.longValue();
		}

	}

	private enum Op {
		SND, SET, ADD, MUL, MOD, RCV, JGZ
	}

	private static class Instruction {
		private Op		op;
		private Target	t1;
		private Target	t2;

		public static Instruction parse(String in) {
			Instruction inst = new Instruction();
			String[] bits = in.split(" ");
			inst.op = Op.valueOf(bits[0].toUpperCase());
			inst.t1 = parseArg(bits[1]);
			if (bits.length == 3)
				inst.t2 = parseArg(bits[2]);
			return inst;
		}

		private static Target parseArg(String in) {
			char ch = in.charAt(0);
			if (ch >= '0' && ch <= '9' || ch == '-') {
				return new TargetInt(Integer.parseInt(in));
			} else {
				return new TargetReg(ch);
			}
		}

		@Override
		public String toString() {
			if (t2 == null)
				return op.name().toLowerCase() + " " + t1;
			else
				return op.name().toLowerCase() + " " + t1 + " " + t2;
		}

	}

	@Override
	public void run(String input, int part, Output out) {
		List<Instruction> lst = Arrays.stream(input.split("\n")).map(Instruction::parse).collect(Collectors.toList());

		if (part == 0) {
			lst.forEach(x -> out.println(x.toString()));
			out.println("");
			out.println("");

			long lastF = 0;
			Context ctx = new Context("main", lst);

			while (ctx.ptr >= 0 && ctx.ptr < lst.size()) {
				int addr = ctx.ptr;
				Instruction inst = ctx.code.get(ctx.ptr++);
				long n;
				switch (inst.op) {
				case SND:
					out.println("Played sound f = " + inst.t1.get(ctx));
					lastF = inst.t1.get(ctx);
					break;
				case SET:
					inst.t1.put(ctx, inst.t2.get(ctx));
					break;
				case ADD:
					n = (long) inst.t1.get(ctx) + inst.t2.get(ctx);
					inst.t1.put(ctx, n);
					break;
				case MUL:
					n = inst.t1.get(ctx) * inst.t2.get(ctx);
					inst.t1.put(ctx, n);
					break;
				case MOD:
					long b = inst.t2.get(ctx);
					if (b != 0)
						n = inst.t1.get(ctx) % b;
					else
						n = 0;
					inst.t1.put(ctx, n);
					break;
				case RCV:
					n = inst.t1.get(ctx);
					if (n != 0) {
						out.println("Recovered " + lastF);
						return;
					}
					break;
				case JGZ:
					n = inst.t1.get(ctx);
					if (n > 0) {
						--ctx.ptr;
						ctx.ptr += inst.t2.get(ctx);
					}
					break;
				}
				out.println(Util.padString(Integer.toString(addr), ' ', 4) + "  " + Util.padString(inst.toString(), ' ', 20) + " [" + ctx.toString() + "]");
			}
		} else if (part == 1) {

			List<Context> contexts = new ArrayList<>();
			contexts.add(new Context("one", lst));
			contexts.add(new Context("two", lst));
			contexts.get(0).sendTo = contexts.get(1);
			contexts.get(1).sendTo = contexts.get(0);
			for (int i = 0; i < contexts.size(); ++i)
				contexts.get(i).putReg('p', i);

			int counter = 0;
			
			//
			// Sod it, just going to copy paste from the first bit rather than restructure anymore!
			//
			for (;;) {
				++counter;
				Context ctx = null;
				if (ctx == null || !ctx.canRun()) {
					// 
					// Find new context that's runnable
					//
					ctx = null;
					for (Context x : contexts)
						if (x.canRun())
							ctx = x;

					if (ctx == null) {
						out.println("Finished after " + counter + " combined operations");
						for (Context c : contexts) 
							out.println("\"" + c.name + "\" sent " + c.sendCounter + " values to its friend");
						return;
					}
				}

				int addr = ctx.ptr;
				Instruction inst = ctx.code.get(ctx.ptr++);
				long n;
				switch (inst.op) {
				case SND:
					ctx.sendTo.queue.add(inst.t1.get(ctx));
					ctx.sendCounter++;
					break;
				case SET:
					inst.t1.put(ctx, inst.t2.get(ctx));
					break;
				case ADD:
					n = (long) inst.t1.get(ctx) + inst.t2.get(ctx);
					inst.t1.put(ctx, n);
					break;
				case MUL:
					n = inst.t1.get(ctx) * inst.t2.get(ctx);
					inst.t1.put(ctx, n);
					break;
				case MOD:
					long b = inst.t2.get(ctx);
					if (b != 0)
						n = inst.t1.get(ctx) % b;
					else
						n = 0;
					inst.t1.put(ctx, n);
					break;
				case RCV:
					if (ctx.queue.isEmpty()) {
						ctx.needInput = true;
						--ctx.ptr; // rewind so it'll attempt this again when it wakes up
					} else {
						long r = ctx.queue.removeFirst();
						inst.t1.put(ctx, r);
					}
					break;
				case JGZ:
					n = inst.t1.get(ctx);
					if (n > 0) {
						--ctx.ptr;
						ctx.ptr += inst.t2.get(ctx);
					}
					break;
				}
//				out.println(ctx.name + ": " + Util.padString(Integer.toString(addr), ' ', 4) + "  " + Util.padString(inst.toString(), ' ', 20) + " [" + ctx.toString() + "]");
			}

		}
	}

}

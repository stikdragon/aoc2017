package uk.co.stikman.aoc.year2016;

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

	//
	// This seems like a human puzzle rather than a computing one.. ?
	//  Step 1 - E up with PoG and ThG
	//  Step 2 - E down 
	//  Step 3 - E up with PrG and RuG
	//  Step 4 - E down
	//  Step 5 - E up with CoG and CoM
	//  Step 6 - E down
	//  Step 7 - E up with RuM
	//  Step 8 - E up with PoG and PoM
	//  Step 9 - E up with PoG and PoM
	//  Step 10 - E down
	//  Step 11 - E down
	//   (repeat 8-11 for Pr/Th/Ru/Co) which is another 14 steps (ignore the last 2 downs)
	// (last step was 24)
	//  
	// Ok that's wrong. not that easy to by hand!  come back to this later i think  
	
	
	
	@Override
	public void run(String input, int part, Output out) {
		if (part == 0) {
		} else if (part == 1) {
		} else
			throw new RuntimeException("Part.. ?");

	}

}

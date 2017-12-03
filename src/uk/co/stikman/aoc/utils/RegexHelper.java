package uk.co.stikman.aoc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {

	private Pattern pattern;

	public RegexHelper(String regex) {
		pattern = Pattern.compile(regex);
	}

	/**
	 * Returns <code>null</code> if the regex didn't match, otherwise the
	 * groups, first one is 0
	 * 
	 * @param text
	 * @return
	 */
	public List<String> exec(String text) {
		Matcher matcher = pattern.matcher(text);
		if (!matcher.matches())
			return null;
		List<String> res = new ArrayList<>();
		for (int i = 0; i < matcher.groupCount(); ++i)
			res.add(matcher.group(i + 1));
		return res;
	}

}

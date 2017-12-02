package uk.co.stikman.aoc.year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SourceData {

	public static String get(int day) {
		File f = new File("data\\2016\\Day" + day + ".txt");
		if (!f.exists())
			throw new RuntimeException("Unknown day: " + day);
		try (InputStream fis = new FileInputStream(f)) {
			BufferedReader rdr = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			String s;
			String sep = "";
			while ((s = rdr.readLine()) != null) {
				sb.append(sep).append(s);
				sep = "\n";
			}
			return sb.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

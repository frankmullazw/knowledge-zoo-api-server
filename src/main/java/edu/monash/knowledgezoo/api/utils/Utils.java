package edu.monash.knowledgezoo.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
	public static String getStringFrom(InputStream in) {
		StringBuilder str = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = reader.readLine()) != null)
				str.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}

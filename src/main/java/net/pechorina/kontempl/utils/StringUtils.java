package net.pechorina.kontempl.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {

	static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * Returns the string trimmed to specified length
	 * 
	 * @param s
	 *            {@link String } String o trim
	 * @param length
	 *            {@link Integer} The max size of return string
	 * @return {@link String }
	 */
	public static String trimToLength(String s, int length) {
		if (s == null || s.isEmpty()) {
			return s;
		}
		s = s.substring(0, Math.min(s.length(), length));
		return s.trim();
	}

	public static boolean stringToBoolean(String str) {
		if (str != null) {
			if (str.trim().equals("1")) {
				return true;
			}
			else if (str.trim().equalsIgnoreCase("true")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Replaces every subsequence of the input string that matches the regexp
	 * with the given replacement string.
	 * 
	 * @return {@link String}
	 */
	public static String simplyRegexpReplace(String input, String regexp,
			String pattern) {
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(input);
        return m.replaceAll(pattern);
	}

	/**
	 * Filter for strings, should be used for preparing filenames before posting
	 * on server. It replace or remove illegal or not safe symbols from the
	 * filename.
	 * 
	 * 
	 */
	public static String prettifyFilename(String name) {
		String lowerUpperCase = "(.{0}(\\p{Lower}\\p{Upper}).{0})+"; // inCap to
																		// in_Cap
		Pattern lowerUpperCasePattern = Pattern.compile(lowerUpperCase);
		Matcher m = lowerUpperCasePattern.matcher(name);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String REPLACE = m.group(1).substring(0, 1) + "_"
					+ m.group(1).substring(1, 2);
			m.appendReplacement(sb, REPLACE);
		}
		m.appendTail(sb);
		name = sb.toString();

		String crazy = "(.{0}(\\{\\[\\(\\<>\\)\\]\\}~\\|\\/).{0})+";// {[(<>)]}~|/
																	// to '-'
		name = simplyRegexpReplace(name, crazy, "-");

		String whiteSpace = "(.{0}(\\s).{0})+"; // whitespace to '_'
		name = simplyRegexpReplace(name, whiteSpace, "_");

		String ampersand = "(.{0}(&).{0})+"; // '&' to "_and_"
		name = simplyRegexpReplace(name, ampersand, "_and_");

		String nonword = "(.{0}([^\\p{Alpha}\\p{Nd}\\-\\._]).{0})+"; // drop
																		// not-word
																		// chars
		name = simplyRegexpReplace(name, nonword, "");

		name = simplyRegexpReplace(name, "(.{0}(_+-+).{0})+", "-");// collapse
																	// _-
																	// sequences
		name = simplyRegexpReplace(name, "(.{0}(_+-+).{0})+", "-");// collapse
																	// -_
																	// sequences
		name = simplyRegexpReplace(name, "(.{0}([\\-\\_]+\\.).{0})+", ".");// collapse
																			// [-_].
																			// sequences
		name = simplyRegexpReplace(name, "(.{0}(\\.[\\-\\_]+).{0})+", ".");// collapse
																			// .[-_]
																			// sequences
		name = simplyRegexpReplace(name, "(.{0}(-{2,}).{0})+", "-");// collapse
																	// repeating
																	// -,
		name = simplyRegexpReplace(name, "(.{0}(_{2,}).{0})+", "_");// collapse
																	// repeating
																	// _,
		name = simplyRegexpReplace(name, "(.{0}(\\.{2,}).{0})+", "_");// collapse
																		// repeating
																		// .,
		name = simplyRegexpReplace(name, "^(\\-|\\_|\\.)+.{0}", "");// remove
																	// leading
																	// -_.
		name = simplyRegexpReplace(name, "(.{0}(\\-|\\_|\\.)+$)", "");// remove
																		// leading
																		// -_.

		// collapse repeating extensions
		Pattern extPattern = Pattern.compile(".{0}\\.(\\w+?)$");
		Matcher mext = extPattern.matcher(name);
		while (mext.find()) {
			name = simplyRegexpReplace(name,
					".{0}(\\." + mext.group(1) + ")+$", "." + mext.group(1));// remove
																				// leading
																				// -_.
		}
		name = name.toLowerCase();
		return name;
	}

	/**
	 * Splits given text to words list. Resulting list contain only words passed
	 * checkStopWords exam.
	 * 
	 * @see checkStopWords
	 * @param @String string
	 * @return {@link List}
	 */
	public static List<String> stringTokenizer(String string) {
		List<String> list = new ArrayList<>();
		if (string == null || string.isEmpty()) {
			return list;
		}

		Pattern p = Pattern.compile("\\b[\\w'-\\.]+\\b");
		Matcher m = p.matcher(string);
		while (m.find()) {
			String s = m.group();
			if (checkStopWords(s)) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * Check given string if it contain a word from stopWordList
	 * 
	 * @param string
	 * @return true if there isn't a stop word, false else
	 */
	public static boolean checkStopWords(String string) {
		String[] stopWord = { "and", "with", "pcs", "for", "pro", "pack", "to",
				"-", "mount", "x" };
        for (String sw : stopWord) {
            if (sw.equals(string.toLowerCase())) {
                return false;
            }
            if (string.matches("^(\\d+)$")) {
                return false;
            }
        }
		return true;
	}

	/**
	 * Removes all non alphanum characters from the string
	 * 
	 * @param filename
	 * @return
	 */
	public static String clearString(String s) {

		return s.replaceAll("[^\\p{Alnum}-_\\.]", "");
	}

	public static String clearPageName(String name) {
		String path = name.trim();
		path = simplyRegexpReplace(path, "\\s+", "-");
		path = simplyRegexpReplace(path, "\\/+", "-");
		path = simplyRegexpReplace(path, "[^A-Za-z0-9\\-]", "");
		path = simplyRegexpReplace(path, "[\\-]{2,}", "-");
		if (path.length() > 80) {
			path = path.substring(0, 79);
		}
		path = path.toLowerCase();

		return path;
	}
	
	public static String clearProductHtmlTitle(String title) {
		if (title == null) return null;
		String r = title.trim();
		r = r.replaceAll("\\n", " ");
		r = r.replaceAll("\\r", " ");
		r = r.replaceAll("\"", "");
		r = r.replaceAll("\'", "");
		if (r.length() > 150) {
			r = r.substring(0, 150);
		}

		return r;
	}
	
	public static String clearDescription(String txt) {
		String res = txt.trim();
		res = simplyRegexpReplace(res, "[\\-]{2,}", "-");
		res = simplyRegexpReplace(res, "[\\s]{2,}", " ");
		res = encodeHtml(res);
		return res;
	}

	public static String convertNameToPath(String name) {
		String path = name.trim();
		path = simplyRegexpReplace(path, "\\s+", "-");
		path = simplyRegexpReplace(path, "[^A-Za-z0-9\\-]", "");
		path = simplyRegexpReplace(path, "[\\-]{2,}", "-");
		if (path.length() > 150) {
			path = path.substring(0, 149);
		}
		return path;
	}

	public static String encodeHtml(CharSequence sequence) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < sequence.length(); i++) {
			char ch = sequence.charAt(i);
			if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN) {
				sb.append(ch);
			} else {
				int codepoint = Character.codePointAt(sequence, i);
				// handle supplementary range chars
				i += Character.charCount(codepoint) - 1;
				// emit entity
				sb.append("&#x");
				sb.append(Integer.toHexString(codepoint));
				sb.append(";");
			}
		}

		return sb.toString();
	}

	public static String convertURLsToLinks(String text) {
		if (text == null) {
			return null;
		}

		return text.replaceAll(
				"(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
				"$1<a rel=\"nofollow\" href=\"$2\">$2</a>$4");
	}

	public static String convertLFtoBRs(String text) {
		if (text == null) {
			return null;
		}
		text = text.replaceAll("\\r", "");
		text = text.trim();
		text = text.replaceAll("\\n{2,}", "\\\n\\\n");
		text = text.replaceAll("\\n", "<br>\\\n");

		return text;
	}
	
	public static String superTrim(String text) {
		if (text == null) {
			return null;
		}
		String out = text.replaceAll("^\\s+", "");
		out = out.replaceAll("\\s+$", "");
		return out;
	}
	
	public static String messageTemplateToMessageCode(String text) {
		if (text == null) {
			return null;
		}

		String out = text.replaceAll("^\\{(.*)\\}$", "$1");
		logger.debug("in:" + text + ", out: " + out);
		return out;
	}

	public static void sortList(List<String> items) {
		Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
	}
	
	public static Map<String, String> sortMapByKey(Map<String, String> items){
		TreeMap<String, String> result = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		result.putAll(items);
		return result;
	}
}

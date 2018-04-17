	public static List<String> extractMatches(String text, String pattern) {
		List<String> matches = new LinkedList<String>();
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text);
		while(m.find()) {
			matches.add(text.subSequence(m.start(), m.end()).toString());
		}
		return matches;
	}

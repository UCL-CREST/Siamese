    public List<String> getAlphanumericWordCharacters(final String input) {
        final List<String> words = new ArrayList<String>();
        if (input != null) {
            final Pattern pat = Pattern.compile("([\\p{L}||\\P{Alpha}&&[^\\p{Punct}]&&[^\\p{Space}]])*");
            final Matcher match = pat.matcher(input);
            while (match.find()) {
                words.add(input.substring(match.start(), match.end()));
            }
        }
        return words;
    }

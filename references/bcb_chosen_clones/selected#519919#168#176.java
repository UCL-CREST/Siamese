    public static Collection<String> getAllMatches(final String input, final String regex, final int flags) {
        final Collection<String> matches = new ArrayList<String>();
        final Pattern pattern = Pattern.compile(regex, flags);
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(input.substring(matcher.start(), matcher.end()));
        }
        return matches;
    }

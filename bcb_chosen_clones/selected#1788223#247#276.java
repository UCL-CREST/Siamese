    private static String readPage(String path, boolean remote) {
        StringBuffer text = new StringBuffer("");
        InputStream in = null;
        try {
            if (remote) in = new URL(path).openConnection().getInputStream(); else in = new FileInputStream(new File(path));
        } catch (Exception e) {
            return null;
        }
        Scanner read = new Scanner(new InputStreamReader(in));
        while (read.hasNext()) {
            text.append(read.next() + " ");
        }
        final String tagRegex = "<.*?>";
        Pattern tagPattern = Pattern.compile(tagRegex);
        Matcher tagMatcher = tagPattern.matcher(text);
        while (tagMatcher.find()) {
            StringBuffer str = new StringBuffer(text.substring(tagMatcher.start(), tagMatcher.end()));
            final String quoteRegex = "\".*?\"|'.*?'";
            Pattern quotePattern = Pattern.compile(quoteRegex);
            Matcher quoteMatcher = quotePattern.matcher(str);
            int i = 0;
            while (quoteMatcher.find()) {
                String toCaps = str.substring(i, quoteMatcher.start());
                str.replace(i, quoteMatcher.start(), toCaps.toUpperCase());
                i = quoteMatcher.end();
            }
            text.replace(tagMatcher.start(), tagMatcher.end(), str.toString());
        }
        return text.toString();
    }

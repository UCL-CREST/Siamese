    public static void match(String regex, String searchString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(searchString);
        boolean found = false;
        int i = 0;
        while (matcher.find()) {
            pl(i + ": I found the text \"" + matcher.group() + "\" starting at " + "index " + matcher.start() + " and ending at index " + matcher.end() + "");
            found = true;
            i++;
        }
        if (!found) {
            pl("No match found for \"" + regex + "\", in: \"" + searchString + "\"");
        }
    }

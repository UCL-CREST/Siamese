    private String processLinks(String input) {
        Pattern openingLinkPattern = Pattern.compile("&lt;a href=.*?&gt;", Pattern.CASE_INSENSITIVE);
        Pattern closingLinkPattern = Pattern.compile("&lt;/a&gt;", Pattern.CASE_INSENSITIVE);
        Matcher closingMatcher = closingLinkPattern.matcher(input);
        input = closingMatcher.replaceAll("</a>");
        Matcher openingMatcher = openingLinkPattern.matcher(input);
        while (openingMatcher.find()) {
            int start = openingMatcher.start();
            int end = openingMatcher.end();
            String link = input.substring(start, end);
            link = "<" + link.substring(4, link.length() - 4) + ">";
            input = input.substring(0, start) + link + input.substring(end, input.length());
            openingMatcher = openingLinkPattern.matcher(input);
        }
        return input;
    }

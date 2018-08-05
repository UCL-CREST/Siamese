    protected String noFollowFy(String text) {
        if (BlojsomUtils.checkNullOrBlank(text)) {
            return text;
        }
        StringBuffer updatedText = new StringBuffer();
        Pattern hyperlinkPattern = Pattern.compile(HYPERLINK_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.DOTALL);
        Matcher hyperlinkMatcher = hyperlinkPattern.matcher(text);
        Pattern attributePattern = Pattern.compile(ATTRIBUTE_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.DOTALL);
        Pattern relAttrPattern = Pattern.compile(REL_ATTR_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.DOTALL);
        Pattern noFollow = Pattern.compile(NOFOLLOW_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.DOTALL);
        Matcher noFollowMatcher;
        Matcher attributeMatcher;
        int lastIndex = 0;
        while (hyperlinkMatcher.find()) {
            updatedText.append(text.substring(lastIndex, hyperlinkMatcher.start()));
            String link = hyperlinkMatcher.group();
            attributeMatcher = attributePattern.matcher(link);
            StringBuffer updatedLink = new StringBuffer();
            boolean shouldAddAttr = true;
            while (attributeMatcher.find()) {
                String attr = attributeMatcher.group();
                Matcher relAttrMatcher = relAttrPattern.matcher(attr);
                while (relAttrMatcher.find()) {
                    noFollowMatcher = noFollow.matcher(attr);
                    if (!noFollowMatcher.matches()) {
                        int indexOfQuote = attr.lastIndexOf("\"");
                        if (indexOfQuote != -1) {
                            attr = attr.substring(0, indexOfQuote) + " nofollow\"";
                            shouldAddAttr = false;
                        }
                    }
                }
                updatedLink.append(attr);
            }
            if (shouldAddAttr) {
                updatedLink.append(REL_NOFOLLOW);
            }
            updatedLink.append((">"));
            updatedText.append(updatedLink);
            lastIndex = hyperlinkMatcher.end();
        }
        updatedText.append(text.substring(lastIndex));
        return updatedText.toString();
    }

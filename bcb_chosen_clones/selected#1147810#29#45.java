    public static String replaceInAttributesOfXMLTags(String source, final String replacementSource, final String replacementTarget) {
        Pattern attribute = Pattern.compile("= *\".*?\"");
        Matcher matcher = attribute.matcher(source);
        int addIndent = 0;
        int lengthDiv = replacementTarget.length() - replacementSource.length();
        while (matcher.find()) {
            String g = matcher.group();
            int localIndent = 0;
            while (g.indexOf(replacementSource) > -1) {
                g = g.replaceFirst(replacementSource, replacementTarget);
                source = source.substring(0, matcher.start() + addIndent) + g + source.substring(matcher.end() + addIndent + localIndent);
                localIndent += lengthDiv;
            }
            addIndent += localIndent;
        }
        return source;
    }

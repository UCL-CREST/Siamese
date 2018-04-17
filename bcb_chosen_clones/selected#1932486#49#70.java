    public static List<String> tokenizeWhere(String where) {
        List<String> split = new ArrayList<String>();
        Pattern pattern = Pattern.compile(conditionRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(where);
        int lastIndex = 0;
        String s;
        int i = 0;
        while (matcher.find()) {
            s = where.substring(lastIndex, matcher.start()).trim();
            logger.finest("value: " + s);
            split.add(s);
            s = matcher.group();
            split.add(s);
            logger.finest("matcher found: " + s + " at " + matcher.start() + " to " + matcher.end());
            lastIndex = matcher.end();
            i++;
        }
        s = where.substring(lastIndex).trim();
        logger.finest("final:" + s);
        split.add(s);
        return split;
    }

    private static String[] getSubQueries(String query) {
        StringBuffer buf = new StringBuffer(query);
        ArrayList<Integer> semicolons = new ArrayList<Integer>();
        Pattern pattern = Pattern.compile("(?:\\\\\\\\)*\\\\" + "'");
        Matcher matcher = pattern.matcher(buf);
        while (matcher.find()) {
            for (int i = matcher.start(); i < matcher.end(); i++) {
                buf.setCharAt(i, '_');
            }
        }
        pattern = Pattern.compile("'.*?'");
        matcher = pattern.matcher(buf);
        while (matcher.find()) {
            for (int i = matcher.start(); i < matcher.end(); i++) {
                buf.setCharAt(i, '_');
            }
        }
        pattern = Pattern.compile(";");
        matcher = pattern.matcher(buf);
        while (matcher.find()) {
            semicolons.add(matcher.start());
        }
        ArrayList<String> subqueries = new ArrayList<String>(semicolons.size() + 1);
        int i = 0;
        int beginIndex = 0;
        for (; i < semicolons.size(); i++) {
            int endIndex = semicolons.get(i);
            String subString = query.substring(beginIndex, endIndex + 1).trim();
            if (subString.length() > 0) {
                subqueries.add(subString);
            }
            beginIndex = endIndex + 1;
        }
        String subString = query.substring(beginIndex, query.length()).trim();
        if (subString.length() > 0) {
            subqueries.add(subString);
        }
        return subqueries.toArray(new String[subqueries.size()]);
    }

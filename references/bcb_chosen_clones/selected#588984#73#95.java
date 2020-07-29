    public void testLucene() {
        Pattern splitQuotPattern = Pattern.compile("[\"][^\"]{0,}[\"]");
        Pattern replaceBlankPattern = Pattern.compile("(?<!(OR)|(AND)|(NOT)|(\\&\\&)|(\\|\\|)|[(\\[,\"]|(TO))[\\s]{1,}(?!(OR)|(AND)|(NOT)|(\\&\\&)|(\\|\\|)|[)\\]\"]|(TO))");
        String queryStr = "+publicAccess:\"1 2\"  NOT \"a TO b\" +(parent:/emcproot* (123, ableflag:3 ) )";
        StringBuffer buffer = new StringBuffer();
        Matcher splitQuotMatcher = splitQuotPattern.matcher(queryStr);
        int lastPos = 0;
        while (splitQuotMatcher.find()) {
            String temp = queryStr.substring(lastPos, splitQuotMatcher.start());
            temp = temp.replaceAll("\\s{1,}", " ");
            Matcher matcher = replaceBlankPattern.matcher(temp);
            buffer.append(matcher.replaceAll(" AND "));
            buffer.append(queryStr.substring(splitQuotMatcher.start(), splitQuotMatcher.end()));
            lastPos = splitQuotMatcher.end();
        }
        if (lastPos < queryStr.length() - 1) {
            String temp = queryStr.substring(lastPos);
            temp = temp.replaceAll("\\s{1,}", " ");
            Matcher matcher = replaceBlankPattern.matcher(temp);
            buffer.append(matcher.replaceAll(" AND "));
        }
        System.out.println(buffer);
    }

    public static String surroundMatchingInString(String sourceString, final String[] matchings, final String pre[], final String post[]) {
        if (matchings.length != pre.length && matchings.length != post.length) {
            throw new IllegalArgumentException("Wrong input data");
        }
        for (int i = 0, n = matchings.length; i < n; i++) {
            String patternString = matchings[i];
            if (patternString.trim().length() > 0) {
                String filterString = "([\\/\\.\\,\\*\\+\\?\\|\\(\\)\\[\\]\\{\\}\\)])";
                String replaceStr = "\\\\$1";
                Pattern prePattern = Pattern.compile(filterString);
                Matcher preMatcher = prePattern.matcher(patternString);
                patternString = preMatcher.replaceAll(replaceStr);
                Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sourceString);
                int matchcount = 0;
                while (matcher.find()) {
                    int shift = (pre[i].length() + post[i].length()) * matchcount;
                    StringBuilder sb = new StringBuilder();
                    sourceString = sb.append(sourceString.substring(0, matcher.start() + shift)).append(pre[i]).append(sourceString.substring(matcher.start() + shift, matcher.end() + shift)).append(post[i]).append(sourceString.substring(matcher.end() + shift, sourceString.length())).toString();
                    matchcount++;
                }
            }
        }
        return sourceString;
    }

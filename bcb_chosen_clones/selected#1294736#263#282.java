    public static String replaceWithGroup(String a_src, String a_findPattern, String a_replace) {
        StringBuffer retour = new StringBuffer();
        Pattern l_pattern = Pattern.compile(a_findPattern);
        String l_src = a_src;
        Matcher l_matcher = l_pattern.matcher(l_src);
        int l_groupCount = l_matcher.groupCount();
        while (l_matcher.find()) {
            String[] l_groups = new String[l_groupCount + 1];
            for (int i = 0; i < l_groupCount + 1; i++) {
                l_groups[i] = l_matcher.group(i);
            }
            String l_replace = replaceGroupIntoExpression(a_replace, l_groups);
            retour.append(l_src.substring(0, l_matcher.start(0)));
            retour.append(l_replace);
            l_src = l_src.substring(l_matcher.end(0));
            l_matcher = l_pattern.matcher(l_src);
        }
        retour.append(l_src);
        return retour.toString();
    }

    public static String expandSubstitutions(String input, Properties map) {
        List names = getExpandableNames(input);
        final Pattern expVarPattern = Pattern.compile("(?<=^\\s*|[^\\$])(\\$\\{.+?\\})");
        Matcher expVarMatcher = expVarPattern.matcher(input);
        StringBuffer sb = new StringBuffer();
        int s = 0;
        int e = 0;
        int i = 0;
        while (expVarMatcher.find()) {
            s = expVarMatcher.start(1);
            sb.append(input.substring(e, s));
            e = expVarMatcher.end(1);
            String replace = expVarMatcher.group(1);
            String key = (String) names.get(i++);
            if (map.containsKey(key)) {
                replace = map.getProperty(key);
            }
            sb.append(replace);
        }
        sb.append(input.substring(e));
        return StringUtils.replace(sb.toString(), "$$", "$");
    }

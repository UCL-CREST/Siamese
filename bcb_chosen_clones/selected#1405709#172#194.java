    public static Map<String, List<int[]>> findUnboundVars(String sqlStmt) {
        Map<String, List<int[]>> notReplaced = new HashMap<String, List<int[]>>();
        String variable;
        Pattern pattern = null;
        String regex = SQL_VAR_BIND_REGEX;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException pex) {
            pex.printStackTrace();
        }
        Matcher matcher = pattern.matcher(sqlStmt);
        while (matcher.find()) {
            variable = matcher.group().substring(1);
            List<int[]> boundsList = notReplaced.get(variable);
            if (boundsList == null) {
                boundsList = new ArrayList<int[]>();
                notReplaced.put(variable, boundsList);
            }
            int[] bounds = { matcher.start(), matcher.end() };
            boundsList.add(bounds);
        }
        return notReplaced;
    }

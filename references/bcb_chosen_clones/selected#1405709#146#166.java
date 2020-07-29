    private List<Integer> nextUnbound(String sqlVar, int start) {
        List<Integer> ret = new ArrayList<Integer>();
        String variable;
        Pattern pattern = null;
        String regex = SQL_VAR_BIND_REGEX;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException pex) {
            pex.printStackTrace();
        }
        Matcher matcher = pattern.matcher(sqlString.substring(start));
        while (matcher.find()) {
            variable = matcher.group().substring(OFFSET);
            if (sqlVar.toLowerCase().equals(variable.toLowerCase())) {
                ret.add(matcher.start() + OFFSET + start);
                ret.add(matcher.end() + OFFSET + start);
                break;
            }
        }
        return ret;
    }

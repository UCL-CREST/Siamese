    public String getMathSubstitute(String expr) {
        String result = "";
        String variable;
        int start, end = 0;
        Pattern pattern = Pattern.compile("\\$[a-zA-Z_0-9]+");
        Matcher matcher = pattern.matcher(expr);
        while (matcher.find()) {
            start = matcher.start();
            result += expr.substring(end, start);
            end = matcher.end();
            variable = expr.substring(start + 1, end);
            result += "(" + getVariablesStore().getValue(variable, mDepth) + ")";
        }
        result += expr.substring(end);
        return result;
    }

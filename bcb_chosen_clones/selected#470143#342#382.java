    private String transform(String expression) throws InvalidOperatorException {
        if (expression == null || expression.length() == 0) {
            throw new IllegalArgumentException("cannot transform undefined expression!");
        }
        StringBuilder sb = new StringBuilder();
        Pattern pat = Pattern.compile("([&|)(!])");
        Matcher matcher = pat.matcher(expression);
        int nextStart = 0;
        while (matcher.find()) {
            String oper = matcher.group();
            int beg = matcher.start();
            int end = matcher.end();
            String match = expression.substring(nextStart, beg).trim();
            if (match.length() > 0) {
                if (OPERATOR_MANAGER.isConditionContainsOperator(match)) {
                    register(match);
                    sb.append(currentConditionName);
                } else {
                    sb.append(match);
                }
            }
            sb.append(oper);
            nextStart = end;
        }
        if (nextStart > 0) {
            String match = expression.substring(nextStart).trim();
            if (match.length() > 0) {
                if (OPERATOR_MANAGER.isConditionContainsOperator(match)) {
                    register(match);
                    sb.append(currentConditionName);
                } else {
                    sb.append(match);
                }
            }
            return sb.toString();
        } else if (OPERATOR_MANAGER.isConditionContainsOperator(expression)) {
            register(expression);
            return currentConditionName;
        }
        return expression;
    }

    public static String evaluateExpression(String expression, VariableEvaluator variableEvaluator) {
        String evaluatedExpression;
        if ((expression != null) && (expression.indexOf("${") >= 0)) {
            Pattern pattern = Pattern.compile("\\$\\{[a-zA-Z_\\._][0-9a-zA-Z_\\._]*\\}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(expression);
            StringBuffer sb = new StringBuffer();
            int ultimoTrozo = 0;
            while (matcher.find() == true) {
                sb.append(expression.substring(ultimoTrozo, matcher.start()));
                String variableName = matcher.group().substring(2, matcher.group().length() - 1);
                Object variableValue = variableEvaluator.getVariableValue(variableName);
                sb.append(variableValue);
                ultimoTrozo = matcher.end();
            }
            sb.append(expression.substring(ultimoTrozo, expression.length()));
            evaluatedExpression = sb.toString();
        } else {
            evaluatedExpression = expression;
        }
        return evaluatedExpression;
    }

    public boolean test(String expression) {
        if (expression != null) {
            String innerVar = "byteData";
            String regex = "\\w+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            StringBuffer javaExpression = new StringBuffer("");
            int index = 0;
            while (matcher.find()) {
                if (index == 0 && matcher.start() > 0) javaExpression.append(expression.substring(0, matcher.start())); else if (index > 0) javaExpression.append(expression.substring(index, matcher.start()));
                javaExpression.append(innerVar).append(".is(\"").append(matcher.group()).append("\")");
                index = matcher.end();
            }
            if (index < expression.length()) javaExpression.append(expression.substring(index));
            ExpressionCompute ep = new FreemarkerExpressionCompute();
            return ep.logicCompute(this, innerVar, javaExpression.toString());
        } else {
            return false;
        }
    }

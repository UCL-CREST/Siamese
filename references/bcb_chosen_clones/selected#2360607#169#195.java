    private Condition<T> compile() throws ParseException {
        final String content = formatExpression(expression);
        if (logger.isDebugEnabled()) {
            logger.debug("parsing expression = " + content);
        }
        Condition<T> conditionExpression = null;
        final Pattern condPattern = Pattern.compile("([nc]term)\\s*(!?=)\\s*([abcxyzip]+)");
        expression = "";
        final Matcher match = condPattern.matcher(content);
        int from = 0;
        while (match.find()) {
            if (logger.isDebugEnabled()) {
                logger.debug("found " + match.group(0));
            }
            expression += content.substring(from, match.start());
            from = match.end();
            final String condName = match.group(1).toLowerCase();
            final String operatorName = match.group(2);
            final String types = match.group(3);
            String opSymbol = "";
            conditionExpression = makeCondition(match, content, condName, operatorName, types, opSymbol);
            engine.register(condName + opSymbol + types, conditionExpression);
            expression += condName + opSymbol + types;
        }
        expression += content.substring(from);
        return engine.translate(expression);
    }

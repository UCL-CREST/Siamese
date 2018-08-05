    protected ICondition getSimpleConditions(String s, ConstrainedConditionGenerator generator, boolean simpleStringCondition) {
        Pattern combinePattern = Pattern.compile(getCombineOperatorRegEx(generator));
        Matcher combinePatternMatcher = combinePattern.matcher(s);
        List<String> simpleConditions = new ArrayList<String>();
        int start = 0;
        int end = 0;
        StringBuffer sb = new StringBuffer(s);
        boolean found = false;
        CombineOperator combineOperator = null;
        while (combinePatternMatcher.find()) {
            String group = combinePatternMatcher.group();
            if (!group.equals("")) {
                found = true;
                combineOperator = generator.getCombineOperator(group);
                end = combinePatternMatcher.start();
                String simpleCondition = sb.substring(start, end);
                simpleConditions.add(simpleCondition);
                start = combinePatternMatcher.end();
            }
        }
        if (end != 0) {
            if (start != sb.length() - 1) {
                String simpleCondition = sb.substring(start, sb.length());
                simpleConditions.add(simpleCondition);
                if (logger.isDebugEnabled()) {
                    logger.debug("simpleCondition = " + simpleCondition);
                }
            }
        }
        if (!found) {
            return parseSimpleCondition(generator, s, simpleStringCondition);
        } else {
            IConditionContainer container = new ConditionContainer();
            container.setCombineOperator(combineOperator);
            for (String string : simpleConditions) {
                ICondition simpleCondition = parseSimpleCondition(generator, string, simpleStringCondition);
                container.addCondition(simpleCondition);
            }
            return container;
        }
    }

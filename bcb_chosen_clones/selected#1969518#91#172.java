    private JPLIConditionalExpression<T> compile() throws JPLParseException {
        JPLCastHookSequence<T, Set<JPLITerminus>> hookSequence = new JPLCastHookSequence<T, Set<JPLITerminus>>();
        if (toPeakTypeHook != null) {
            hookSequence.addHook(toPeakTypeHook);
        }
        JPLICastHook<JPLIMSnPeakType, Set<JPLITerminus>> toTerminiHook = new JPLICastHook<JPLIMSnPeakType, Set<JPLITerminus>>() {

            public Set<JPLITerminus> cast(JPLIMSnPeakType object) {
                Set<JPLITerminus> s = new HashSet<JPLITerminus>();
                s.add(object.getNTerm());
                s.add(object.getCTerm());
                return s;
            }
        };
        hookSequence.addHook(toTerminiHook);
        String content = formatExpression(expression);
        if (logger.isDebugEnabled()) {
            logger.debug("parsing expression = " + content);
        }
        JPLIConditionalExpression<T> conditionExpression = null;
        Pattern condPattern = Pattern.compile("([nNcC]t(?:erm)?)\\s*(!?=)\\s*([abcxyzipP]+)");
        expression = "";
        Matcher match = condPattern.matcher(content);
        int from = 0;
        while (match.find()) {
            boolean not = false;
            if (logger.isDebugEnabled()) {
                logger.debug("found " + match.group(0));
            }
            expression += content.substring(from, match.start());
            from = match.end();
            String condName = match.group(1).toLowerCase();
            String operatorName = match.group(2);
            String types = match.group(3);
            if (condName.charAt(0) == 'n') {
                Pattern typePattern = Pattern.compile(".*([abc]+).*");
                Matcher typeMatch = typePattern.matcher(types);
                if (typeMatch.find()) {
                    throw new JPLParseException("in condition '" + content + "', bad N-terminus condition: " + " invalid " + typeMatch.group(1) + " type", match.start(3) + typeMatch.start(1));
                }
            } else {
                Pattern typePattern = Pattern.compile(".*([xyz]+).*");
                Matcher typeMatch = typePattern.matcher(types);
                if (typeMatch.find()) {
                    throw new JPLParseException("bad C-terminus condition: " + " invalid " + typeMatch.group(1) + " type", match.start(3) + typeMatch.start(1));
                }
            }
            String opSymbol = "";
            if (operatorName.equals("!=")) {
                opSymbol = "_not";
                not = true;
            }
            OPERATOR operator;
            if (types.length() > 1) {
                Set<JPLITerminus> value = new HashSet<JPLITerminus>();
                operator = OPERATOR.INTER;
                opSymbol += "_inter_";
                for (int i = 0; i < types.length(); i++) {
                    JPLITerminus term = getTerminus(types.charAt(i), (condName.charAt(0) == 'n'));
                    ((Set<JPLITerminus>) value).add(term);
                }
                if (not) {
                    conditionExpression = new JPLCondition.Builder<T, Set<JPLITerminus>>(value).castHook(hookSequence).operator(operator).not().build();
                } else {
                    conditionExpression = new JPLCondition.Builder<T, Set<JPLITerminus>>(value).castHook(hookSequence).operator(operator).build();
                }
            } else {
                JPLITerminus value = getTerminus(types.charAt(0), (condName.charAt(0) == 'n'));
                operator = OPERATOR.CONTAINS;
                opSymbol += "_contains_";
                if (not) {
                    conditionExpression = new JPLCondition.Builder<T, JPLITerminus>(value).castHook(hookSequence).operator(operator).not().build();
                } else {
                    conditionExpression = new JPLCondition.Builder<T, JPLITerminus>(value).castHook(hookSequence).operator(operator).build();
                }
            }
            engine.addSymbol(condName + opSymbol + types, conditionExpression);
            expression += condName + opSymbol + types;
        }
        expression += content.substring(from);
        return engine.compile(expression);
    }

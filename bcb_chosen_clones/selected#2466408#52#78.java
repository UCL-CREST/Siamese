    public static final String mergeArguments(final String args, final String valueSeparator, final String argumentSeparator, final boolean acceptSingleArgs) {
        HashMap<String, String> argumentsMap = new HashMap<String, String>();
        StringBuilder arguments = new StringBuilder();
        String arg;
        Pattern keyValue = Pattern.compile("(?:^|\\s)([^\\\"\\s]+)=\"([^\\\"]+)\"");
        Matcher matcherKV = keyValue.matcher(args);
        int last = 0;
        while (matcherKV.find()) {
            arguments.append(args.subSequence(last, matcherKV.start()));
            last = matcherKV.end();
            if (argumentsMap.containsKey(matcherKV.group(1))) {
                arg = argumentsMap.get(matcherKV.group(1)) + valueSeparator + matcherKV.group(2);
                argumentsMap.put(matcherKV.group(1), arg);
            } else {
                argumentsMap.put(matcherKV.group(1), matcherKV.group(2));
            }
        }
        if (argumentsMap.size() == 0) return args;
        arguments.append(args.substring(last));
        if (!acceptSingleArgs) {
            arguments = new StringBuilder();
        }
        for (Entry<String, String> m : argumentsMap.entrySet()) {
            arguments.append(argumentSeparator + m.getKey() + "=\"" + m.getValue() + "\"");
        }
        return arguments.toString();
    }

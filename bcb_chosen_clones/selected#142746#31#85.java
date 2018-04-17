    public Query(String query) throws QuerySyntaxError {
        int groupSpecial = 1;
        int groupContent = 2;
        Pattern commentPattern = Pattern.compile("^" + any(SPACE) + literal("#") + optional(group(literal("!"))) + group(any(".")) + "$", Pattern.MULTILINE);
        Matcher matcher = commentPattern.matcher(query);
        groupOperations = new ArrayList<Grouping>();
        int level = 0;
        StringBuilder buffer = new StringBuilder();
        int idx = 0;
        while (matcher.find(idx)) {
            buffer.append(query.substring(idx, matcher.start()));
            if (matcher.group(groupSpecial) != null) {
                String commandLine = matcher.group(groupContent);
                int groupGroup = 1;
                int headingGroup = 2;
                int hideGroup = 3;
                Pattern commandPattern = Pattern.compile("^" + any(SPACE) + choice(group(literal("group")), group(literal("heading")), group(literal("hide"))) + literal(":") + any(SPACE));
                Matcher commandMatcher = commandPattern.matcher(commandLine);
                if (!commandMatcher.lookingAt()) {
                    throw new QuerySyntaxError("Invalid command line: Expected command in '" + commandLine + "'");
                }
                String commandArg = commandLine.substring(commandMatcher.end());
                if (commandMatcher.group(groupGroup) != null) {
                    String[] columns = commandArg.split(any(SPACE) + literal(",") + any(SPACE));
                    groupOperations.add(new Group(columns));
                } else if (commandMatcher.group(hideGroup) != null) {
                    String[] columns = commandArg.split(any(SPACE) + literal(",") + any(SPACE));
                    for (String column : columns) {
                        hiddenColumns.add(column);
                    }
                } else if (commandMatcher.group(headingGroup) != null) {
                    int varGroup = 1;
                    Pattern varPattern = Pattern.compile(literal("${") + group(many(notIn("}"))) + literal("}"));
                    List<String> columns = new ArrayList<String>();
                    Matcher varMatcher = varPattern.matcher(commandArg);
                    int varIdx = 0;
                    while (varMatcher.find(varIdx)) {
                        String varName = varMatcher.group(varGroup);
                        columns.add(varName);
                        varIdx = varMatcher.end();
                    }
                    int columnCount = columns.size();
                    if (columnCount == 0) {
                        throw new QuerySyntaxError("Invalid heading command: Missing variable reference in heading command.");
                    }
                    groupOperations.add(new Heading(columns.toArray(new String[columnCount]), level++, commandArg));
                } else {
                    throw new AssertionError("Unreachable.");
                }
            }
            idx = matcher.end();
        }
        buffer.append(query.substring(idx, query.length()));
        sql = buffer.toString();
    }

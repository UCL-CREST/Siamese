    private Pattern createPattern(String pattern) {
        StringBuilder patternBuilder = new StringBuilder();
        Matcher m = GLOB_PATTERN.matcher(pattern);
        int end = 0;
        while (m.find()) {
            patternBuilder.append(quote(pattern, end, m.start()));
            String match = m.group();
            if ("?".equals(match)) {
                patternBuilder.append('.');
            } else if ("*".equals(match)) {
                patternBuilder.append(".*");
            } else if (match.startsWith("{") && match.endsWith("}")) {
                int colonIdx = match.indexOf(':');
                if (colonIdx == -1) {
                    patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                    variableNames.add(m.group(1));
                } else {
                    String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
                    patternBuilder.append('(');
                    patternBuilder.append(variablePattern);
                    patternBuilder.append(')');
                    String variableName = match.substring(1, colonIdx);
                    variableNames.add(variableName);
                }
            }
            end = m.end();
        }
        patternBuilder.append(quote(pattern, end, pattern.length()));
        return Pattern.compile(patternBuilder.toString());
    }

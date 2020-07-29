    private void parseTemplate() {
        Pattern protocolPattern = Pattern.compile("[^:]*:[^:]*:");
        String template2 = "";
        Matcher matcher = protocolPattern.matcher(template);
        if (matcher.find()) {
            jdbcProtocol = template.substring(0, matcher.end());
            template2 = template.substring(matcher.end(), template.length());
        }
        matcher = PATTERN.matcher(template2);
        int start = 0;
        while (matcher.find()) {
            String part = template2.substring(start, matcher.start());
            parts.add(part);
            String var = matcher.group(1);
            variables.add(var);
            varMap.put(var, null);
            start = matcher.end();
        }
        if (start < template2.length()) {
            parts.add(template2.substring(start));
        }
    }

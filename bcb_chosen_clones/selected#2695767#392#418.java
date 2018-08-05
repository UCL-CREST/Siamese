    @VisibleForTesting
    static Map<String, Object> convertInstrumentResult(String result) {
        Map<String, Object> map = Maps.newHashMap();
        Pattern pattern = Pattern.compile("^INSTRUMENTATION_(\\w+): ", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(result);
        int previousEnd = 0;
        String previousWhich = null;
        while (matcher.find()) {
            if ("RESULT".equals(previousWhich)) {
                String resultLine = result.substring(previousEnd, matcher.start()).trim();
                int splitIndex = resultLine.indexOf("=");
                String key = resultLine.substring(0, splitIndex);
                String value = resultLine.substring(splitIndex + 1);
                map.put(key, value);
            }
            previousEnd = matcher.end();
            previousWhich = matcher.group(1);
        }
        if ("RESULT".equals(previousWhich)) {
            String resultLine = result.substring(previousEnd, matcher.start()).trim();
            int splitIndex = resultLine.indexOf("=");
            String key = resultLine.substring(0, splitIndex);
            String value = resultLine.substring(splitIndex + 1);
            map.put(key, value);
        }
        return map;
    }

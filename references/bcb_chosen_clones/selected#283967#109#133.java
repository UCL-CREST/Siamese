    public static String[] splitPreserveAllTokens(String input, String regex) {
        int index = 0;
        Pattern p = Pattern.compile(regex);
        ArrayList<String> result = new ArrayList<String>();
        Matcher m = p.matcher(input);
        int lastBeforeIdx = 0;
        while (m.find()) {
            if (StringUtils.isNotEmpty(m.group())) {
                String match = input.subSequence(index, m.start()).toString();
                if (StringUtils.isNotEmpty(match)) {
                    result.add(match);
                }
                result.add(input.subSequence(m.start(), m.end()).toString());
                index = m.end();
            }
        }
        if (index == 0) {
            return new String[] { input };
        }
        final String remaining = input.subSequence(index, input.length()).toString();
        if (StringUtils.isNotEmpty(remaining)) {
            result.add(remaining);
        }
        return result.toArray(new String[result.size()]);
    }

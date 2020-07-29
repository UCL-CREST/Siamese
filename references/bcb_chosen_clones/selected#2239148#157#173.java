    private String[] splitContent(String matchexp, String content) {
        int startAt = 0;
        List tempList = new ArrayList();
        Pattern pattern = Pattern.compile(matchexp);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            tempList.add(content.substring(startAt, matcher.start()));
            tempList.add(matcher.group());
            startAt = matcher.end();
        }
        tempList.add(content.substring(startAt));
        String[] result = new String[tempList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tempList.get(i);
        }
        return result;
    }

    public static String insertURLS(String input) {
        String regexp = "(\\%)+URL\\([^ \\)]*\\)";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(input);
        int start = 0;
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            int startGroup = matcher.start();
            int endGroup = matcher.end();
            String group = matcher.group();
            result.append(input.substring(start, startGroup));
            start = matcher.end();
            while (group.startsWith("%%")) {
                group = group.substring(2);
                startGroup = startGroup + 2;
                result.append("%");
            }
            if (group.startsWith("%URL")) {
                String url = input.substring(startGroup + 5, endGroup - 1);
                result.append("<%=renderResponse.encodeURL(renderRequest.getContextPath()+ \"" + url + "\")%>");
            } else result.append(input.substring(startGroup, endGroup));
        }
        if (input.length() > start) result.append(input.substring(start, input.length()));
        return result.toString();
    }

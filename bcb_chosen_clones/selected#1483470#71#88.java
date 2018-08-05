    public String doTranslate(String variable) {
        String content = variable;
        Pattern p = Pattern.compile(regExpPattern);
        Matcher m = p.matcher(variable);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String match = variable.substring(start, end);
            for (int i = 0; i < parametersList.size(); i++) {
                Vector paramter = (Vector) parametersList.get(i);
                if (match.indexOf((String) paramter.get(0)) >= 0) {
                    String value = (String) paramter.get(1);
                    content = content.replaceAll(match, value);
                }
            }
        }
        return content;
    }

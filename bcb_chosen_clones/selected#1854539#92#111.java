    protected static String findJavacOutputs(String fullOutput) {
        if (fullOutput == null) return "";
        StringBuilder javacOutputs = new StringBuilder();
        int findOffset = 0;
        Pattern javacPattern = Pattern.compile("^\\s*\\[javac\\]", Pattern.MULTILINE);
        Matcher m = javacPattern.matcher(fullOutput);
        while (m.find(findOffset)) {
            int javacBegin = m.end();
            int javacEnd = fullOutput.length();
            Pattern taskPattern = Pattern.compile("^\\s*\\[[^\\]]+\\]", Pattern.MULTILINE);
            Matcher m2 = taskPattern.matcher(fullOutput);
            if (m2.find(javacBegin)) {
                javacEnd = m2.start();
            }
            javacOutputs.append(fullOutput.substring(javacBegin, javacEnd));
            if (javacEnd == fullOutput.length()) break;
            findOffset = javacEnd;
        }
        return javacOutputs.toString();
    }

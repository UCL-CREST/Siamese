    private String replaceDotsButNotDotDotsInString(Widget w, String initialValue) {
        String resultValue = initialValue;
        if (initialValue.contains("..")) {
            resultValue = resultValue.replaceAll("\\.\\.", "XXXXX");
        }
        Pattern decimalPattern = Pattern.compile("[0-9]\\.[0-9]");
        Matcher m = decimalPattern.matcher(resultValue);
        int count = 0;
        while (m.find()) {
            resultValue = resultValue.replace(resultValue.substring(m.start(), m.end()), resultValue.substring(m.start(), m.end()).replace(".", "~"));
        }
        resultValue = resultValue.replaceAll("\\.", "@" + w.getDescription());
        if (initialValue.contains("..")) {
            resultValue = resultValue.replaceAll("XXXXX", "..");
        }
        if (resultValue.contains("~")) {
            resultValue = resultValue.replaceAll("~", ".");
        }
        return resultValue;
    }

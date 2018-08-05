    public void performRegEx(final String exp, final String text) {
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(text);
        fStyles.clear();
        int start = 0;
        int end = 0;
        while (matcher.find()) {
            end = matcher.start();
            fStyles.addElement(new StyleRange(start, end - start, nonMatchForegroundColor, nonMatchBackgroundColor));
            start = matcher.start();
            end = matcher.end();
            fStyles.addElement(new StyleRange(start, end - start, matchForegroundColor, matchBackgroundColor));
            start = end;
        }
        end = text.length();
        fStyles.addElement(new StyleRange(start, end - start, nonMatchForegroundColor, nonMatchBackgroundColor));
    }

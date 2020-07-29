    public void doRegex() {
        if (dfRegex.getText().trim().length() == 0 || taSource.getText().trim().length() == 0) {
            epTarget.setText("<h2>ERROR</h2>Please specify the regex and text to parse.");
            return;
        }
        try {
            Pattern pattern = Pattern.compile(dfRegex.getText());
            Matcher matcher = pattern.matcher(taSource.getText());
            boolean found = false;
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                found = true;
                String source = taSource.getText();
                String first = source.substring(0, matcher.start());
                String last = source.substring(matcher.end(), source.length());
                String match = matcher.group();
                String res = escape(first) + "<font color=\"#FF0000\"><b><u>" + escape(match) + "</u></b></font>" + escape(last);
                sb.append("<code><pre bgcolor=\"#E0E0E0\">" + res + "</pre></code>");
            }
            if (!found) {
                epTarget.setText("<h2>RESULT</h2>Regex not found !!!!. Please refine your regex.");
            } else {
                epTarget.setText("<h2>RESULT</h2>" + sb.toString());
            }
        } catch (PatternSyntaxException pse) {
            epTarget.setText("<h2>INVALID REGEX</h2>" + pse.getMessage());
        }
    }

    private void updateResult() {
        final String text = fText.getText();
        final Pattern pattern = Pattern.compile(fRegEx.getText());
        final Matcher matcher = pattern.matcher(text);
        final StringBuffer result = new StringBuffer();
        int start = 0;
        int end = 0;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            result.append(text.substring(start, end));
            result.append(' ');
            result.append(start);
            result.append(' ');
            result.append(end);
            result.append('\n');
        }
        Display display = fText.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                fResult.setText(result.toString());
            }
        });
    }

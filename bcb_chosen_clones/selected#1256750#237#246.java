    private void parseArithmeticExpression(String expression) {
        Pattern numberPattern = Pattern.compile(NUMBER);
        Matcher matcher = numberPattern.matcher(expression);
        int lastEnd = 0;
        while (matcher.find(lastEnd)) {
            this.output += expression.substring(lastEnd, matcher.start());
            this.output += "<span class=\"rubynumber\">" + matcher.group() + "</span>";
            lastEnd = matcher.end();
        }
    }

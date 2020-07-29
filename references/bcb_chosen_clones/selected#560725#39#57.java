    private String filterLatexExpressions(String text) {
        String pattern = "(\\$([^\\$]+)\\$)|(\\$\\$[^\\$]+\\$\\$)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            boolean isInline = true;
            String formula = text.substring(m.start(), m.end());
            if (formula.startsWith("$$")) {
                formula = text.substring(m.start() + 2, m.end() - 2);
                isInline = false;
            } else formula = text.substring(m.start() + 1, m.end() - 1);
            String replacement = oxdoc.config.MathProcessor.ProcessFormula(formula, isInline);
            Object[] args = { isInline ? "expression" : "equation", replacement };
            replacement = MessageFormat.format("<span class=\"{0}\">{1}</span>", args);
            m.appendReplacement(myStringBuffer, Matcher.quoteReplacement(replacement));
        }
        return m.appendTail(myStringBuffer).toString();
    }

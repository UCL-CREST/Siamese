    static String formatExpression(final String expression) throws ParseException {
        final Pattern pattern = Pattern.compile("([(!]*)\\s*(!?=)?" + "\\s*([abcxyzip]+(?:\\s*,\\s*[abcxyzip]{1,2})*)\\s*(\\)*)" + "\\s*([|&])?\\s*");
        final Matcher match = pattern.matcher(expression);
        boolean found = false;
        final StringBuffer sb = new StringBuffer();
        String binOperator = null;
        int shift = 0;
        int openParenthesis = 0;
        int closedParenPos = 0;
        while (match.find()) {
            String term = "term";
            String op = "=";
            found = true;
            if (logger.isDebugEnabled()) {
                logger.debug(shift + ": '" + match.group(0) + "' found ! ");
            }
            if (match.group(1) != null) {
                openParenthesis += match.group(1).replace("!", "").length();
                sb.append(match.group(1));
            }
            if (match.group(2) != null) {
                op = match.group(2);
            }
            if (term.equals("term")) {
                final String[] types = match.group(3).split(",");
                for (final String type : types) {
                    try {
                        sb.append(toTermString(type, op));
                    } catch (final ParseException e) {
                        throw new ParseException(e.getMessage() + " '" + expression + "': parse exception", shift + match.start(3));
                    }
                    sb.append(" | ");
                }
                sb.delete(sb.length() - 2, sb.length());
            } else {
                sb.append(term);
                sb.append(op);
                sb.append(match.group(3));
            }
            if (match.group(4) != null) {
                closedParenPos = match.end(4);
                openParenthesis -= match.group(4).length();
                sb.append(match.group(4));
            }
            binOperator = match.group(5);
            if (binOperator != null) {
                sb.append(" " + binOperator + " ");
            }
            if (logger.isDebugEnabled()) {
                for (int i = 1; i <= match.groupCount(); i++) {
                    logger.debug("group " + i + ": " + match.group(i));
                }
            }
            shift = match.end();
        }
        if (!found) {
            throw new ParseException("bad peak type conditional expression: " + expression, 0);
        } else if (openParenthesis != 0) {
            final boolean beg = (openParenthesis > 0) ? true : false;
            throw new ParseException(((beg) ? "missing" : "remove") + " token ')'" + " in conditional expression: '" + expression + "'", closedParenPos);
        }
        return sb.toString();
    }

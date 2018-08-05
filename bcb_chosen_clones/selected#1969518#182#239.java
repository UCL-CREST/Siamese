    static String formatExpression(String expression) throws JPLParseException {
        Pattern pattern = Pattern.compile("([(!]*)\\s*([nNcC]?t(?:erm)?)?\\s*(!?=)" + "\\s*([abcxyzipP]+(?:\\s*,\\s*[abcxyzipP]{1,2})*)\\s*(\\)*)" + "\\s*([|&])?\\s*");
        Matcher match = pattern.matcher(expression);
        boolean found = false;
        StringBuffer sb = new StringBuffer();
        String binOperator = null;
        int shift = 0;
        int openParenthesis = 0;
        int closedParenPos = 0;
        while (match.find()) {
            found = true;
            if (logger.isDebugEnabled()) {
                logger.debug(shift + ": '" + match.group(0) + "' found ! ");
            }
            if (match.group(1) != null) {
                openParenthesis += match.group(1).replace("!", "").length();
                sb.append(match.group(1));
            }
            if (match.group(2).equals("term")) {
                String[] types = match.group(4).split(",");
                for (String type : types) {
                    try {
                        sb.append(toTermString(type, match.group(3)));
                    } catch (JPLParseException e) {
                        throw new JPLParseException("'" + expression + "': parse exception", shift + match.start(4), e);
                    }
                    sb.append(" | ");
                }
                sb.delete(sb.length() - 2, sb.length());
            } else {
                sb.append(match.group(2));
                sb.append(match.group(3));
                sb.append(match.group(4));
            }
            if (match.group(5) != null) {
                closedParenPos = match.end(5);
                openParenthesis -= match.group(5).length();
                sb.append(match.group(5));
            }
            binOperator = match.group(6);
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
            throw new JPLParseException("bad peak type conditional expression: " + expression, 0);
        } else if (openParenthesis != 0) {
            boolean beg = (openParenthesis > 0) ? true : false;
            throw new JPLParseException(((beg) ? "missing" : "remove") + " token ')'" + " in conditional expression: '" + expression + "'", closedParenPos);
        }
        return sb.toString();
    }

    protected String parseTimeFormatString(String input) throws ParseException {
        final String formatPattern = "(([yMDdHmsS])\\2*)";
        final String delimiterPattern = "([-/:.,_ \t]+)";
        final String literalPattern = "('(?:[^']|'')*')";
        Pattern token = Pattern.compile(formatPattern + "|" + delimiterPattern + "|" + literalPattern);
        int from = 0;
        StringBuffer frmtString = new StringBuffer();
        Matcher matcher = token.matcher(input);
        while (matcher.find(from)) {
            int start = matcher.start();
            if (start > from) {
                char[] dots = new char[start + 1];
                java.util.Arrays.fill(dots, from, start, '.');
                dots[from] = '^';
                dots[start] = '^';
                StringBuffer errorString = new StringBuffer("Unrecognized sub-pattern\n");
                errorString.append(input).append("\n");
                errorString.append(dots);
                throw new ParseException(errorString.toString(), from);
            }
            String frmt = matcher.group(1);
            String delimiter = matcher.group(3);
            String literal = matcher.group(4);
            if (frmt != null) {
                switch(frmt.charAt(0)) {
                    case 'y':
                        {
                            appendSubFormat(frmtString, YEAR_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 'M':
                        {
                            appendSubFormat(frmtString, MONTH_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 'D':
                        {
                            appendSubFormat(frmtString, DOY_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 'd':
                        {
                            appendSubFormat(frmtString, DAY_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 'H':
                        {
                            appendSubFormat(frmtString, HOUR_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 'm':
                        {
                            appendSubFormat(frmtString, MINUTE_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 's':
                        {
                            appendSubFormat(frmtString, SECONDS_FIELD_INDEX, frmt.length());
                        }
                        break;
                    case 'S':
                        {
                            int digitCount = frmt.length();
                            int fieldIndex = addScaleFactor(digitCount);
                            appendSubFormat(frmtString, fieldIndex, digitCount);
                        }
                        break;
                    default:
                        break;
                }
            } else if (delimiter != null) {
                frmtString.append(delimiter);
            } else if (literal != null) {
                literal = literal.substring(1, literal.length() - 1);
                literal = literal.replaceAll("''", "'");
                frmtString.append(literal);
            }
            from = matcher.end();
        }
        return frmtString.toString();
    }

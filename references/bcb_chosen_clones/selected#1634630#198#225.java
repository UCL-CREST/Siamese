    private void replaceConstants(Map aMap) {
        if (hasConstants()) {
            Pattern pattern = Pattern.compile("%(\\w+)%");
            for (Iterator iter = aMap.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String stmt = (String) entry.getValue();
                Matcher matcher = pattern.matcher(stmt);
                StringBuffer sb = new StringBuffer(stmt.length() * 2);
                int offset = 0;
                while (matcher.find()) {
                    sb.append(stmt.substring(offset, matcher.start()));
                    String constant = matcher.group(1);
                    String replacementValue = (String) resolveToken(constant);
                    if (replacementValue != null) {
                        sb.append(replacementValue);
                    } else {
                        sb.append(matcher.group());
                        Exception e = new Exception(MessageFormat.format("Missing constant'{0}' found in SQL configuration file.", new Object[] { constant }));
                        logger.error("Error parsing SQL configuration file: Missing constant " + constant + " found in SQL configuration file.");
                        SdlException.logError(e, "Error parsing SQL configuration file.");
                    }
                    offset = matcher.end();
                }
                if (offset < stmt.length()) sb.append(stmt.substring(offset));
                entry.setValue(sb.toString());
            }
        }
    }

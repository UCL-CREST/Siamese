    public static String find(String source, Object findPattern, boolean mark) {
        StringBuffer buf = new StringBuffer(source);
        Pattern p = Pattern.compile(findPattern.toString());
        Matcher m = p.matcher(buf);
        int i = 1;
        while (m.find()) {
            logger.debug("\t Found " + i++ + ". " + m.group());
            for (int j = 1; j < m.groupCount(); j++) {
                logger.debug("\t\t Group " + j + ". " + m.group(j));
            }
            if (mark) {
                buf.insert(m.start(), FIND_MARK_CHAR);
                buf.insert(m.end() + 1, FIND_MARK_CHAR);
            }
        }
        return buf.toString();
    }

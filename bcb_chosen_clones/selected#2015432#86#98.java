    public static String makeMultiLineToolTip(String original) {
        StringBuilder sb = new StringBuilder();
        if (!original.trim().toLowerCase().startsWith("<html>")) sb.append("<html>");
        Pattern p = Pattern.compile("(.{0,80}\\b\\s*)");
        Matcher m = p.matcher(original);
        if (m.find()) sb.append(original.substring(m.start(), m.end()).trim());
        while (m.find()) {
            sb.append("<br>");
            sb.append(original.substring(m.start(), m.end()).trim());
        }
        if (!original.trim().toLowerCase().endsWith("</html>")) sb.append("</html>");
        return sb.toString();
    }

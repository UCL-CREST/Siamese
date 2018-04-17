    public static String replacePlaceholders(String str, String placeholder, String replacement) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("(\\$\\{" + placeholder + "\\})");
        int lastIndex = 0;
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            sb.append(str.substring(lastIndex, m.start()));
            sb.append(replacement);
            lastIndex = m.end();
        }
        sb.append(str.substring(lastIndex));
        return sb.toString();
    }

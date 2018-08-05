    public static String format(String json) {
        json = json.replaceAll("\\\\\"", "").replaceAll("\\\\\'", "");
        int offset = 0;
        StringBuilder buffer = new StringBuilder(json);
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        while (m.find()) {
            buffer.replace(m.start(), m.end(), "'" + m.group(1).replace("'", " ") + "'");
        }
        json = buffer.toString();
        json = json.replaceAll("\\s+:", ":").replaceAll(":\\s+", ":");
        json = json.replaceAll("\\s+,", ",").replaceAll(",\\s+", ",");
        p = Pattern.compile("'(\\w+)':");
        m = p.matcher(json);
        buffer.delete(0, buffer.length()).append(json);
        while (m.find()) {
            buffer.deleteCharAt(m.start() - offset);
            offset++;
            buffer.deleteCharAt(m.end() - 2 - offset);
            offset++;
        }
        p = Pattern.compile(":([a-zA-Z_]+)([,\\]\\}])");
        m = p.matcher(buffer.toString());
        offset = 0;
        while (m.find()) {
            buffer.insert(m.start() + 1 + offset, '\'');
            offset++;
            buffer.insert(m.end() - 1 + offset, '\'');
            offset++;
        }
        return buffer.toString();
    }

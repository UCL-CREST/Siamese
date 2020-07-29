    private String replaceSettings(String path) {
        String regexp = "\\$\\{setting:(.*)\\}";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(path);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String setting = path.substring(m.start() + 10, m.end() - 1);
            String replacedby = getSitoolsSetting(setting);
            if (replacedby != null) {
                m.appendReplacement(sb, replacedby);
            } else {
                m.appendReplacement(sb, "");
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

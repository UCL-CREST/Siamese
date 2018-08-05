    public static String convert2WinStyle(String inn) {
        StringBuffer sb = new StringBuffer(inn);
        Pattern p = Pattern.compile("\\${1}[1-9a-zA-Z]+");
        Matcher m = p.matcher(inn);
        int increased = 0;
        while (m.find()) {
            sb.replace(m.start() + increased, m.start() + increased + 1, "%");
            sb.insert(m.end() + increased, "%");
            increased++;
        }
        return sb.toString().replace('/', '\\');
    }

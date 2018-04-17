    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("{{{a}}}b{{{c}}}");
        Pattern p = Pattern.compile("\\{\\{\\{.*?\\}\\}\\}");
        Matcher m = p.matcher(sb);
        while (m.find()) {
            String s = sb.substring(m.start(), m.end());
            System.out.println(s);
        }
    }

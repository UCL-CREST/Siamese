    public static String removeHearImp(String text, String start, String end) {
        String res = text;
        Pattern p = Pattern.compile("\\" + start + ".*?" + "\\" + end);
        Matcher m = p.matcher(res);
        while (m.find()) {
            res = res.substring(0, m.start()) + res.substring(m.end(), res.length());
            m = p.matcher(res);
        }
        return res;
    }

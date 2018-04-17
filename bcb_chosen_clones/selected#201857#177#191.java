    private ArrayList<String> getCsvMenu(String s) {
        Pattern p = Pattern.compile("(?<!\\\\),");
        Matcher m = p.matcher(s);
        ArrayList<String> al = new ArrayList<String>();
        if (m.find()) {
            int end, start = 0;
            do {
                end = m.start();
                al.add(s.substring(start, end).replaceAll("\\\\,", ","));
                start = m.end();
            } while (m.find());
            if (end < s.length()) al.add(s.substring(start, s.length()).replaceAll("\\\\,", ","));
        } else if (s.length() > 0) al.add(s);
        return al;
    }

    public ArrayList<String> retrieve(String s, String regexp) {
        ArrayList<String> matches = new ArrayList<String>();
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(s);
        while (m.find()) {
            matches.add(s.substring(m.start(), m.end()));
        }
        return matches;
    }

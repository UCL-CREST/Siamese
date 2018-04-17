    public String checkString(String s) {
        Pattern p = null;
        Matcher m = null;
        int start = 0;
        int end = 0;
        for (int i = 0; i < comps.size(); i++) {
            try {
                p = Pattern.compile(comps.get(i));
            } catch (PatternSyntaxException pse) {
                ;
            }
            m = p.matcher(s);
            while (m.find()) {
                start = m.start();
                end = m.end();
                if (start == 0 && end == s.length()) {
                    s = reps.get(i);
                } else if (start == 0) {
                    s = reps.get(i) + s.substring(end);
                } else if (end == s.length()) {
                    s = s.substring(0, start) + reps.get(i);
                } else {
                    s = s.substring(0, start) + reps.get(i) + s.substring(end);
                }
            }
        }
        return s;
    }

    static void examine(String s, String regex) {
        Display d = new Display(regex);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        while (m.find()) d.display("find() '" + m.group() + "' start = " + m.start() + " end = " + m.end());
        if (m.lookingAt()) d.display("lookingAt() start = " + m.start() + " end = " + m.end());
        if (m.matches()) d.display("matches() start = " + m.start() + " end = " + m.end());
    }

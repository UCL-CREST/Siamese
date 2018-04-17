    public static String processLine(String line) {
        line = line.replace("^$/$$", "\\$");
        line = line.replace("^//$", "\\/");
        line = line.replace("^[/[$", "\\[");
        line = line.replace("^]/]$", "\\]");
        line = line.replace("^{/{$", "\\{");
        line = line.replace("^}/}$", "\\}");
        Pattern pattern = Pattern.compile("(\\^./.[<[a-zA-Z0-9]+>]?\\$)");
        Matcher matcher = pattern.matcher(line);
        List<String> changes = new LinkedList<String>();
        while (matcher.find()) {
            int groupcount = matcher.groupCount();
            for (int i = 1; i <= groupcount; i++) {
                String m = line.substring(matcher.start(i), matcher.end(i));
                String[] msplit = m.split("/");
                if (msplit[0].charAt(1) == msplit[1].charAt(0)) changes.add(m);
            }
        }
        for (String s : changes) {
            line = line.replace(s, "" + s.charAt(1));
        }
        return line;
    }

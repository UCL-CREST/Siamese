    public NamedPattern(final String namedRegex, final int flags) {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("\\((\\{(\\S+?)})");
        Matcher m = p.matcher(namedRegex);
        int pos = 0;
        while (m.find()) {
            groups.add(m.group(2));
            sb.append(namedRegex.substring(pos, m.start(1)));
            pos = m.end();
        }
        String regex = sb.append(namedRegex.substring(pos)).toString();
        pattern = Pattern.compile(regex, flags);
    }

    public DataSourceImpl(List<String> data) {
        final Pattern p = Pattern.compile("\\{[-+#!*/]?");
        for (String s : data) {
            final StringTokenizer st = new StringTokenizer(s, "|}", true);
            while (st.hasMoreTokens()) {
                final String token = st.nextToken().trim();
                if (token.equals("|")) {
                    continue;
                }
                final Terminator terminator = st.hasMoreTokens() ? Terminator.NEWCOL : Terminator.NEWLINE;
                final Matcher m = p.matcher(token);
                final boolean found = m.find();
                if (found == false) {
                    addInternal(token, terminator);
                    continue;
                }
                int lastStart = 0;
                int end = 0;
                do {
                    final int start = m.start();
                    if (start > lastStart) {
                        addInternal(token.substring(lastStart, start), Terminator.NEWCOL);
                    }
                    end = m.end();
                    final Terminator t = end == token.length() ? terminator : Terminator.NEWCOL;
                    addInternal(token.substring(start, end), t);
                    lastStart = end;
                } while (m.find());
                if (end < token.length()) {
                    addInternal(token.substring(end), terminator);
                }
            }
        }
    }

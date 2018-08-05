    public Pattern(LanguageDescription languageDescription, String pattern) {
        languageDescription.getClass();
        pattern.getClass();
        try {
            prolog.loadLibrary(new PatternLibrary());
        } catch (InvalidLibraryException e) {
            throw new InitializationException("An InvalidLibraryException occurred while trying to initialize pattern", e);
        }
        this.languageDescription = languageDescription;
        String[] ps = pattern.split(SPLIT_REGEX, -1);
        boolean ct = false;
        java.util.regex.Pattern varpat = java.util.regex.Pattern.compile(VAR_REGEX);
        for (String p : ps) {
            if (ct) {
                try {
                    Theory theory = new Theory(p + NEWLINE);
                    patternTheories.add(theory);
                } catch (InvalidTheoryException e) {
                    throw new InitializationException("An InvalidTheoryException occurred when parsing the pattern, see cause for details", e);
                }
            } else {
                Matcher m = varpat.matcher(p);
                int first = 0;
                while (m.find()) {
                    String raw = p.substring(first, m.start());
                    parts.add(processRawString(raw));
                    parts.add(new Variable(this, p.substring(m.start() + 1, m.end() - 1)));
                    first = m.end();
                }
                if (first < p.length()) {
                    String raw = p.substring(first);
                    parts.add(processRawString(raw));
                }
            }
            ct = !ct;
        }
        if (patternTheories.size() == 0) try {
            patternTheories.add(new Theory("cond([]). "));
        } catch (InvalidTheoryException e) {
            throw new InitializationException("An InvalidTheoryException occurred", e);
        }
    }

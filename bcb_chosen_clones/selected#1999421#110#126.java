    public static List<IRegion> getElExpressionRegions(String in) {
        final Pattern braces = Pattern.compile("[{]([^}]*)[}]");
        final Pattern legalFirstChar = Pattern.compile("^[$_a-zA-Z].*");
        final List<IRegion> list = new ArrayList<IRegion>();
        int nextFindStart = 0;
        Matcher m = braces.matcher(in);
        while (m.find(nextFindStart)) {
            String fieldReference = m.group(1);
            if (!legalFirstChar.matcher(fieldReference).matches()) {
                nextFindStart = m.start() + 2;
                continue;
            }
            list.add(new Region(m.start(1), m.end(1) - m.start(1)));
            nextFindStart = m.end();
        }
        return list;
    }

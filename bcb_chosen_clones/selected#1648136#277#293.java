    public void testMultiple() {
        Pattern pattern;
        Matcher matcher;
        boolean _found = false;
        String _regex = "hell.";
        String _in = "hello mom";
        pattern = Pattern.compile(_regex);
        matcher = pattern.matcher(_in);
        while (matcher.find()) {
            System.out.println(" : Text \"" + matcher.group() + "\" start :  " + matcher.start() + " end : " + matcher.end() + ".");
            _found = true;
        }
        if (!_found) {
            System.out.println("No match found.");
        }
        assertTrue(_found);
    }

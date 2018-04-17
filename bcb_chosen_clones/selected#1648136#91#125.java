    public void testBoundaryMatches() {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("^Hello\\w*", Pattern.CASE_INSENSITIVE);
        m = p.matcher("Hello how are you doing");
        boolean _found = false;
        while (m.find()) {
            System.out.println(" : Boundary-Search \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
        m = p.matcher("Helloksjkld how are you doing");
        _found = false;
        while (m.find()) {
            System.out.println(" : Boundary-Search \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
        p = Pattern.compile("\\bHello\\B", Pattern.CASE_INSENSITIVE);
        m = p.matcher("This is not fun Hello ksjkldhow are you doing");
        _found = false;
        while (m.find()) {
            System.out.println(" : New-Search \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertFalse(_found);
        p = Pattern.compile("\\bHello\\b", Pattern.CASE_INSENSITIVE);
        m = p.matcher("This is not fun Hello ksjkldhow are you doing");
        _found = false;
        while (m.find()) {
            System.out.println(" : New-Search \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
    }

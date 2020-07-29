    public void testEnd() {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("spiritbot\\W", Pattern.CASE_INSENSITIVE);
        m = p.matcher("spiritbot: how are you doing");
        boolean _found = false;
        while (m.find()) {
            System.out.println(" : Text \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
    }

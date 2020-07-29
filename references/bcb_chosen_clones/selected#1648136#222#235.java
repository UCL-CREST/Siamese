    public void testSentFront() {
        String botRecord = "hello";
        String msg = "hellohow are you";
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile(".*?hel", Pattern.CASE_INSENSITIVE);
        m = p.matcher(msg);
        boolean _found = false;
        while (m.find()) {
            System.out.println(" : Text \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
    }

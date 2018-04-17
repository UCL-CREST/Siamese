    public void testExtractFullSent() {
        String cmd = "hello how are";
        String msgFull = "Hello How Are You";
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile(cmd, Pattern.CASE_INSENSITIVE);
        m = p.matcher(msgFull);
        BotLogger.log(" ;; Check Full Compare ;");
        boolean _found = false;
        while (m.find()) {
            System.out.println(" : Text \"" + m.group() + "\" start :  " + m.start() + " end : " + m.end() + ".");
            _found = true;
        }
        assertTrue(_found);
    }

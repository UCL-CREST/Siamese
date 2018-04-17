    public void testExtractFront() {
        String cmd = "sendmsg hello this is fun";
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("\\s", Pattern.CASE_INSENSITIVE);
        m = p.matcher(cmd);
        String results[] = p.split(cmd);
        boolean _found = false;
        int str = -1;
        int end = -1;
        while (m.find()) {
            str = m.start();
            end = m.end();
            _found = true;
            break;
        }
        BotLogger.log("--+" + results[0] + " :" + cmd.substring(end, cmd.length()).trim());
    }

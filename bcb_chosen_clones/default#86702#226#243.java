    public boolean runCommand(String pCommand) throws Exception {
        boolean error = false;
        String cmdtext = pCommand;
        System.out.print("\n[ " + cmdtext);
        String[] cmd = ArgUtil.splitQuoted(cmdtext);
        cmd = TextUtil.quoteSpaces(cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        String line_out = null;
        BufferedReader br_out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (null != (line_out = br_out.readLine())) {
            if (null != line_out) {
            }
        }
        int result = 0;
        boolean success = (!error) && (0 == result);
        System.out.print(" ]");
        return success;
    }

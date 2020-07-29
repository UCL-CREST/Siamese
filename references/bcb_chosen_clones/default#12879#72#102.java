    private static String execCmd(String cmd) {
        String msg = new String("");
        boolean cmdOK = true;
        Process proc = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            proc = runtime.exec(cmd);
            InputStreamReader isrError = new InputStreamReader(proc.getErrorStream());
            BufferedReader brError = new BufferedReader(isrError);
            String line = null;
            while ((line = brError.readLine()) != null) {
                System.out.println("ERROR MSG>" + line);
            }
            InputStreamReader isrOutput = new InputStreamReader(proc.getInputStream());
            BufferedReader brOutput = new BufferedReader(isrOutput);
            line = null;
            while ((line = brOutput.readLine()) != null) {
                System.out.println("OUTPUT>" + line);
            }
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Exception e) {
            cmdOK = false;
            msg += "Problem executing command: " + cmd + "  DAMN. Exception " + e.toString();
            System.err.println(msg);
        }
        if (cmdOK) {
            msg += "Command: " + cmd + " OK.";
        }
        return msg;
    }

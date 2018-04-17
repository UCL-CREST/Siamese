    public String runBlast(String cmd, BufferedWriter mlBuffWtr) {
        String msg = new String("");
        boolean cmdOK = true;
        Process proc = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            System.out.println("COMMAND> " + cmd);
            proc = runtime.exec("nice " + cmd);
            BlastToXMLConverter blastParser = new BlastToXMLConverter();
            BufferedWriter blastOutWtr = new BufferedWriter(new FileWriter(workingDir + "/blast.out"));
            InputStreamReader isrOutput = new InputStreamReader(proc.getInputStream());
            BufferedReader brOutput = new BufferedReader(isrOutput);
            blastParser.parse(brOutput, mlBuffWtr, blastOutWtr);
            InputStreamReader isrError = new InputStreamReader(proc.getErrorStream());
            BufferedReader brError = new BufferedReader(isrError);
            String line = null;
            while ((line = brError.readLine()) != null) {
                System.out.println("ERROR MSG>" + line);
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

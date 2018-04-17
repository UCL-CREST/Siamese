    public static boolean startProcess(String strWorkingDirectory, String pProcessCommand, boolean pBackground) {
        Process pProcess = null;
        boolean bSuccess = true;
        if (strWorkingDirectory == null) {
            strWorkingDirectory = System.getProperty("user.dir");
        }
        try {
            String osName = System.getProperty("os.name");
            String strExecStmt;
            if (osName.startsWith("Windows")) {
                strExecStmt = "cmd.exe /c " + pProcessCommand;
            } else {
                strExecStmt = pProcessCommand;
            }
            File x = new File(strWorkingDirectory);
            pProcess = Runtime.getRuntime().exec(strExecStmt, null, x);
        } catch (Exception e) {
            System.out.println("Error running exec(): " + e.getMessage());
            return false;
        }
        try {
            if (pBackground == false) {
                BufferedReader in = new BufferedReader(new InputStreamReader(pProcess.getInputStream()));
                String currentLine = null;
                while ((currentLine = in.readLine()) != null) System.out.println(currentLine);
                BufferedReader err = new BufferedReader(new InputStreamReader(pProcess.getErrorStream()));
                while ((currentLine = err.readLine()) != null) System.out.println(currentLine);
                int iReturnValue = pProcess.waitFor();
                if (iReturnValue != 0) {
                    bSuccess = false;
                }
            } else {
                bSuccess = true;
            }
        } catch (Exception e) {
            System.out.println("Error in process: " + e.getMessage());
            return false;
        }
        return bSuccess;
    }

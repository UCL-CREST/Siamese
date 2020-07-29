    private Object[] exec(Object[] commandLine) {
        Object[] result = new Object[2];
        result[0] = "No command specified";
        result[1] = "Execute";
        if (commandLine == null || commandLine.length < 1) return result;
        String command = (String) commandLine[0];
        result[1] = command;
        String whitelist;
        if (Files.exists("Colander.whitelist") == false) {
            result[0] = "Execution of native commands is disabled";
            return result;
        }
        try {
            whitelist = Files.read("Colander.whitelist", "UTF-8");
        } catch (IOException ex) {
            result[0] = "Error accessing whitelist: " + ex.toString();
            return result;
        }
        boolean match = false;
        StringTokenizer st = new StringTokenizer(whitelist, "\n\r");
        String token;
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            if (token.compareTo(command) == 0) {
                match = true;
                break;
            }
        }
        if (match == false) {
            result[0] = "The specified command is not allowed on this server";
            return result;
        }
        StringBuffer outputString = new StringBuffer();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(command);
            BufferedReader cmdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader cmdOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String sErr = cmdError.readLine();
            String sOut = cmdOutput.readLine();
            while (sErr != null || sOut != null) {
                if (sErr != null) {
                    outputString.append(sErr + newline);
                    sErr = cmdError.readLine();
                }
                if (sOut != null) {
                    outputString.append(sOut + newline);
                    sOut = cmdOutput.readLine();
                }
            }
            cmdError.close();
            cmdOutput.close();
        } catch (IOException ex) {
            outputString = new StringBuffer("Execute error: " + ex.toString());
        }
        result[0] = outputString;
        return result;
    }

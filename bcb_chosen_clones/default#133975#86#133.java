    private String getResponse(URLConnection con) {
        BufferedReader br = null;
        String statusLine = "";
        int shellExitStatus = 0;
        String line = null;
        try {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = br.readLine()) != null) {
                statusLine = line;
            }
            br.close();
        } catch (IOException e) {
            try {
                if (isLinuxSystem()) {
                    ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
                    Process shell = builder.start();
                    br = new BufferedReader(new InputStreamReader(shell.getInputStream()));
                    shellExitStatus = shell.waitFor();
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    System.out.println();
                    br.close();
                } else {
                    if (isWindowsSystem()) {
                        Process shell = Runtime.getRuntime().exec("cmd /c" + "\"" + command + "\"");
                        br = new BufferedReader(new InputStreamReader(shell.getInputStream()));
                        shellExitStatus = shell.waitFor();
                        while ((line = br.readLine()) != null) {
                            System.out.println(line + "\n");
                        }
                        System.out.println();
                        br.close();
                    } else {
                        System.out.println("StatusChecker: OS not known.");
                    }
                }
                if (!smtpServer.equals("")) {
                    sendEMail(null, null);
                }
                System.exit(0);
            } catch (Exception e1) {
                System.err.println("StatusChecker: Error while call " + "extern Program.");
                e1.printStackTrace();
            }
        }
        return statusLine;
    }

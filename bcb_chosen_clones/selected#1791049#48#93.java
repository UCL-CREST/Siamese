    protected void performTask() {
        logger.debug("Started " + this.getClass().getSimpleName());
        if (this.tomcatBinaryPath == null) {
            modifyStartTomcatFile();
            updateTomcatServerConfigPort(this.httpsPort + "");
            this.tomcatBinaryPath = this.cfg.getOrgNodePath() + File.separator + "tomcat" + this.pvtOrPubKey + File.separator + "bin";
        }
        logger.debug("Starting tomcat in " + this.tomcatBinaryPath);
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("cmd /K echo Started Shell");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            PrintWriter out = new PrintWriter(bw, true);
            out.println("cd /D " + tomcatBinaryPath);
            out.println("caties_startup.bat");
            out.println("exit");
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    if (line.endsWith("exit")) {
                        break;
                    }
                }
                proc.waitFor();
                int eValue = proc.exitValue();
                System.out.println("Got exit value of " + eValue);
                in.close();
                out.close();
                proc.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("Completed " + this.getClass().getSimpleName());
    }

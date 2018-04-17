    public void cleanUp() throws Exception {
        File workDirFile = (new File(this.dataWorkDir)).getAbsoluteFile();
        String cmd = "/bin/rm " + this.filesCreated;
        this.filesCreated = "";
        this.info = null;
        System.out.println("cleanUp:\n" + cmd);
        Process p = Runtime.getRuntime().exec(cmd, null, workDirFile);
        ProcessOutputHandler.create(p);
        while (true) {
            try {
                p.waitFor();
                break;
            } catch (InterruptedException e) {
            } finally {
                if (p != null) {
                    closehandle(p.getOutputStream());
                    closehandle(p.getInputStream());
                    closehandle(p.getErrorStream());
                    p.destroy();
                }
            }
        }
        this.deleteWorkDirectory();
    }

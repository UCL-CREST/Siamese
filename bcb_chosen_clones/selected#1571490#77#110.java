    public boolean getFiles(String pRemoteDirectory, String pLocalDirectory) throws IOException {
        final String methodSignature = "boolean getFiles(String,String): ";
        FTPClient fc = new FTPClient();
        fc.connect(getRemoteHost());
        fc.login(getUserName(), getPassword());
        fc.changeWorkingDirectory(pRemoteDirectory);
        FTPFile[] files = fc.listFiles();
        boolean retrieved = false;
        logInfo("Listing Files: ");
        int retrieveCount = 0;
        File tmpFile = null;
        for (int i = 0; i < files.length; i++) {
            tmpFile = new File(files[i].getName());
            if (!tmpFile.isDirectory()) {
                FileOutputStream fos = new FileOutputStream(pLocalDirectory + "/" + files[i].getName());
                retrieved = fc.retrieveFile(files[i].getName(), fos);
                if (false == retrieved) {
                    logInfo("Unable to retrieve file: " + files[i].getName());
                } else {
                    logInfo("Successfully retrieved file: " + files[i].getName());
                    retrieveCount++;
                }
                if (null != fos) {
                    fos.flush();
                    fos.close();
                }
            }
        }
        logInfo("Retrieve count: " + retrieveCount);
        if (retrieveCount > 0) {
            return true;
        }
        return false;
    }

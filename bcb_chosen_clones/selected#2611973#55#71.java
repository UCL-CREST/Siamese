    public void execute() {
        File sourceFile = new File(oarfilePath);
        File destinationFile = new File(deploymentDirectory + File.separator + sourceFile.getName());
        try {
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(destinationFile);
            byte[] readArray = new byte[2048];
            while (fis.read(readArray) != -1) {
                fos.write(readArray);
            }
            fis.close();
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            logger.severe("failed to copy the file:" + ioe);
        }
    }

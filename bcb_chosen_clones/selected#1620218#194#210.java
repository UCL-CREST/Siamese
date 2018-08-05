    public boolean downloadFile(String sourceFilename, String targetFilename) throws RQLException {
        checkFtpClient();
        InputStream in = null;
        try {
            in = ftpClient.retrieveFileStream(sourceFilename);
            if (in == null) {
                return false;
            }
            FileOutputStream target = new FileOutputStream(targetFilename);
            IOUtils.copy(in, target);
            in.close();
            target.close();
            return ftpClient.completePendingCommand();
        } catch (IOException ex) {
            throw new RQLException("Download of file with name " + sourceFilename + " via FTP from server " + server + " failed.", ex);
        }
    }

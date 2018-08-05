    public void uploadFile(String filename) throws RQLException {
        checkFtpClient();
        OutputStream out = null;
        try {
            out = ftpClient.storeFileStream(filename);
            IOUtils.copy(new FileReader(filename), out);
            out.close();
            ftpClient.completePendingCommand();
        } catch (IOException ex) {
            throw new RQLException("Upload of local file with name " + filename + " via FTP to server " + server + " failed.", ex);
        }
    }

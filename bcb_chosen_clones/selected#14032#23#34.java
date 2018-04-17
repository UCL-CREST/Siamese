    public String readFile(String filename) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(USERNAME, PASSWORD);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean success = ftpClient.retrieveFile(filename, outputStream);
        ftpClient.disconnect();
        if (!success) {
            throw new IOException("Retrieve file failed: " + filename);
        }
        return outputStream.toString();
    }

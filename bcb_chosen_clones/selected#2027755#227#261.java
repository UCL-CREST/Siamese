    private static void getFileFtp(String user, String password, String host, int port, String fileName, String location) throws Exception {
        Log.info("\nretrieve " + fileName + NEW_LINE);
        FTPClient client = new FTPClient();
        client.connect(host, port);
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new Exception("FTP fail to connect");
        }
        if (!client.login(user, password)) {
            throw new Exception("FTP fail to login");
        }
        try {
            File locationFile = new File(location);
            File dest = new File(locationFile, fileName);
            if (dest.exists()) {
                dest.delete();
            } else {
                locationFile.mkdirs();
            }
            boolean status = client.changeWorkingDirectory("/");
            Log.info("chdir-status:" + status + NEW_LINE);
            client.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalActiveMode();
            InputStream in = client.retrieveFileStream(fileName);
            if (in == null) {
                Log.error("Input stream is null\n");
                throw new Exception("Fail to retrieve file " + fileName);
            }
            Thread.sleep(3000);
            saveInputStreamToFile(in, new File(location, fileName));
        } finally {
            client.disconnect();
        }
    }

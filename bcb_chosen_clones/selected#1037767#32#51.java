    @Override
    public void download(String remoteFilePath, String localFilePath) {
        InputStream remoteStream = null;
        try {
            remoteStream = client.get(remoteFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream localStream = null;
        try {
            localStream = new FileOutputStream(new File(localFilePath));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            IOUtils.copy(remoteStream, localStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

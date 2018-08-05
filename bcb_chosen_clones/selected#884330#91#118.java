    public boolean storeFile(String local, String remote) throws IOException {
        boolean stored = false;
        GridFTP ftp = new GridFTP();
        ftp.setDefaultPort(port);
        System.out.println(this + ".storeFile " + remote);
        try {
            ftp.connect(host);
            ftp.login(username, password);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return false;
            }
            ftp.put(local, remote);
            ftp.logout();
            stored = true;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stored;
    }

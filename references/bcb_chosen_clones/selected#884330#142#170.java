    public boolean getFile(String local, String remote) throws IOException {
        boolean result = false;
        GridFTP ftp = new GridFTP();
        ftp.setDefaultPort(port);
        System.out.println(this + ".getFile " + remote);
        try {
            ftp.connect(host);
            ftp.login(username, password);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return false;
            }
            ftp.get(local, remote);
            ftp.logout();
            result = true;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(this + ".getFile return " + result);
        return result;
    }

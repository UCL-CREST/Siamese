    public static String ftpPing(String root, String host, int port, String username, String pw) {
        try {
            ftpClient = new FTPClient();
            ftpClient.setRemoteAddr(InetAddress.getByName(host));
            ftpClient.setControlPort(port);
            ftpClient.setTimeout(4000);
            ftpClient.connect();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ftpClient.login(username, (pw));
            ftpClient.chdir(root);
            JobHandler.releaseFTPLock();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (FTPException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        try {
            ftpClient.quit();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (FTPException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

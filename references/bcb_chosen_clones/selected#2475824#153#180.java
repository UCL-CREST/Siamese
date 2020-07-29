    public final int connectAndLogin(Uri u, boolean cwd) throws UnknownHostException, IOException, InterruptedException {
        if (ftp.isLoggedIn()) {
            if (cwd) {
                String path = u.getPath();
                if (path != null) ftp.setCurrentDir(path);
            }
            return WAS_IN;
        }
        int port = u.getPort();
        if (port == -1) port = 21;
        String host = u.getHost();
        if (ftp.connect(host, port)) {
            if (theUserPass == null || theUserPass.isNotSet()) theUserPass = new FTPCredentials(u.getUserInfo());
            if (ftp.login(theUserPass.getUserName(), theUserPass.getPassword())) {
                if (cwd) {
                    String path = u.getPath();
                    if (path != null) ftp.setCurrentDir(path);
                }
                return LOGGED_IN;
            } else {
                ftp.logout(true);
                ftp.disconnect();
                Log.w(TAG, "Invalid credentials.");
                return NO_LOGIN;
            }
        }
        return NO_CONNECT;
    }

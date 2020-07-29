    private boolean initConnection() {
        try {
            if (ftp == null) {
                ftp = new FTPClient();
                serverIP = getServer();
                userName = getUserName();
                password = getPassword();
            }
            ftp.connect(serverIP);
            ftp.login(userName, password);
            return true;
        } catch (SocketException a_excp) {
            throw new RuntimeException(a_excp);
        } catch (IOException a_excp) {
            throw new RuntimeException(a_excp);
        } catch (Throwable a_th) {
            throw new RuntimeException(a_th);
        }
    }

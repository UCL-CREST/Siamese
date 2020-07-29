    public static void uploadFile(String localPath, String hostname, String username, String password, String remotePath) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(hostname);
            int reply = ftpClient.getReplyCode();
            boolean success = false;
            if (FTPReply.isPositiveCompletion(reply)) {
                success = ftpClient.login(username, password);
                if (!success) {
                    Output.error("Failed to login with username/password " + username + "/" + password);
                    return;
                }
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            }
            FileInputStream in = new FileInputStream(localPath);
            boolean result = ftpClient.storeFile(remotePath, in);
            if (!result) {
                Output.error("Logged in but failed to upload " + localPath + " to " + remotePath + "\nPerhaps one of the paths was wrong.");
            }
            in.close();
            ftpClient.disconnect();
        } catch (IOException ioe) {
            Output.error("Error ftp'ing using " + "\nlocalPath: " + localPath + "\nhostname: " + hostname + "\nusername: " + username + "\npassword: " + password + "\nremotePath: " + remotePath, ioe);
        }
    }

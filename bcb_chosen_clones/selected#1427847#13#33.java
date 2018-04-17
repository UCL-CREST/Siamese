    public void getDataFiles(String server, String username, String password, String folder, String destinationFolder) {
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(server);
            ftp.login(username, password);
            System.out.println("Connected to " + server + ".");
            System.out.print(ftp.getReplyString());
            ftp.enterLocalActiveMode();
            ftp.changeWorkingDirectory(folder);
            System.out.println("Changed to " + folder);
            FTPFile[] files = ftp.listFiles();
            System.out.println("Number of files in dir: " + files.length);
            for (int i = 0; i < files.length; i++) {
                getFiles(ftp, files[i], destinationFolder);
            }
            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

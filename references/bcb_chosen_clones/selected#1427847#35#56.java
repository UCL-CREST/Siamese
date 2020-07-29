    public void putDataFiles(String server, String username, String password, String folder, String destinationFolder) {
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(server);
            System.out.println("Connected");
            ftp.login(username, password);
            System.out.println("Logged in to " + server + ".");
            System.out.print(ftp.getReplyString());
            ftp.changeWorkingDirectory(destinationFolder);
            System.out.println("Changed to directory " + destinationFolder);
            File localRoot = new File(folder);
            File[] files = localRoot.listFiles();
            System.out.println("Number of files in dir: " + files.length);
            for (int i = 0; i < files.length; i++) {
                putFiles(ftp, files[i]);
            }
            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

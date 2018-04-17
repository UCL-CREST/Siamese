    @Override
    public void run() {
        try {
            FTPClient ftp = new FTPClient();
            try {
                ftp.connect("localhost", 21);
                ftp.login("ftpuser", "ftpuser123");
                System.out.println("Current: " + ftp.printWorkingDirectory());
                System.out.println("Dir status: " + ftp.makeDirectory(DIR));
                ftp.changeWorkingDirectory(DIR);
                System.out.println("File status: " + ftp.storeFile(FILE_PREFIX + this.getName(), getByteInputStream()));
            } finally {
                ftp.disconnect();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(args.length);
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        try {
            ftp.connect("localhost");
            ftp.login("ethan", "ethan");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            InputStream input;
            input = new FileInputStream("d:/tech/webwork-2.2.7.zip");
            boolean is = ftp.storeFile("backup/webwork-2.2.7.zip", input);
            input.close();
            System.out.println(is);
            FTPFile[] files = ftp.listFiles("backup");
            for (FTPFile ftpFile : files) {
                long time = ftpFile.getTimestamp().getTimeInMillis();
                long days = (System.currentTimeMillis() - time) / (1000 * 60 * 60 * 24);
                if (days > 30) {
                    System.out.println(ftpFile.getName() + "is a old file");
                    ftp.deleteFile("backup/" + ftpFile.getName());
                } else {
                    System.out.println(ftpFile.getName() + "is a new file");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftp.logout();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

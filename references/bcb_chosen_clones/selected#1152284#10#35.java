    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            client.connect("192.168.1.10");
            client.login("a", "123456");
            String[] names = client.listNames();
            for (String name : names) {
                System.out.println("Name = " + name);
            }
            FTPFile[] ftpFiles = client.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getType() == FTPFile.FILE_TYPE) {
                    System.out.println("FTPFile: " + ftpFile.getName() + "; " + FileUtils.byteCountToDisplaySize(ftpFile.getSize()));
                }
            }
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

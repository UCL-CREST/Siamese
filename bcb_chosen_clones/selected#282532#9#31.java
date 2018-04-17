    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        try {
            client.connect("192.168.1.10");
            client.login("a", "123456");
            String filename = "D:\\DHTH5CLT\\HK3\\Ung dung phan tan\\FTP_JAVA\\FTP_DETAI\\FTP\\src\\DemoFTP\\filename\\5s.txt";
            fis = new FileInputStream(filename);
            client.storeFile(filename, fis);
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

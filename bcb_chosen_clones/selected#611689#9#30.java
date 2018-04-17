    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        FileOutputStream fos = null;
        try {
            client.connect("192.168.1.10");
            client.login("a", "123456");
            String filename = "i.exe";
            fos = new FileOutputStream(filename);
            client.retrieveFile("/" + filename, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

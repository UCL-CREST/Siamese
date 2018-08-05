    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            client.connect("192.168.1.10");
            boolean login = client.login("a", "123456");
            if (login) {
                System.out.println("Dang nhap thanh cong...");
                boolean logout = client.logout();
                if (logout) {
                    System.out.println("Da Logout khoi FTP Server...");
                }
            } else {
                System.out.println("Dang nhap that bai...");
            }
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

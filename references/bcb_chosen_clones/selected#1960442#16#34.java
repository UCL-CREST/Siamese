    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        String sFTP = "ftp.servidor.com";
        String sUser = "usuario";
        String sPassword = "pasword";
        try {
            System.out.println("Conectandose a " + sFTP);
            client.connect(sFTP);
            client.login(sUser, sPassword);
            System.out.println(client.printWorkingDirectory());
            client.changeWorkingDirectory("\\httpdocs");
            System.out.println(client.printWorkingDirectory());
            System.out.println("Desconectando.");
            client.logout();
            client.disconnect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

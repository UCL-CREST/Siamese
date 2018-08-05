    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        String sFTP = "ftp.miservidor.com";
        String sUser = "usuario";
        String sPassword = "password";
        try {
            System.out.println("Conectandose a " + sFTP);
            client.connect(sFTP);
            boolean login = client.login(sUser, sPassword);
            if (login) {
                System.out.println("Login correcto");
                boolean logout = client.logout();
                if (logout) {
                    System.out.println("Logout del servidor FTP");
                }
            } else {
                System.out.println("Error en el login.");
            }
            System.out.println("Desconectando.");
            client.disconnect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

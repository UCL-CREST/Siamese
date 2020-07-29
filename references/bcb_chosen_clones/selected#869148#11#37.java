    public static void main(String[] args) {
        try {
            FTPClient p = new FTPClient();
            p.connect("url");
            p.login("login", "pass");
            int sendCommand = p.sendCommand("SYST");
            System.out.println("TryMe.main() - " + sendCommand + " (sendCommand)");
            sendCommand = p.sendCommand("PWD");
            System.out.println("TryMe.main() - " + sendCommand + " (sendCommand)");
            sendCommand = p.sendCommand("NOOP");
            System.out.println("TryMe.main() - " + sendCommand + " (sendCommand)");
            sendCommand = p.sendCommand("PASV");
            System.out.println("TryMe.main() - " + sendCommand + " (sendCommand)");
            p.changeWorkingDirectory("/");
            try {
                printDir(p, "/");
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.logout();
            p.disconnect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

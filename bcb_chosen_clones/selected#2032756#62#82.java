    private boolean createFTPConnection() {
        client = new FTPClient();
        System.out.println("Client created");
        try {
            client.connect(this.hostname, this.port);
            System.out.println("Connected: " + this.hostname + ", " + this.port);
            client.login(username, password);
            System.out.println("Logged in: " + this.username + ", " + this.password);
            this.setupActiveFolder();
            return true;
        } catch (IllegalStateException ex) {
            Logger.getLogger(FTPProject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FTPProject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPIllegalReplyException ex) {
            Logger.getLogger(FTPProject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPException ex) {
            Logger.getLogger(FTPProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

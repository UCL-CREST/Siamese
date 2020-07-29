    public boolean deploy(MMedia[] media) {
        if (this.getIP_Address().equals("127.0.0.1") || this.getName().equals("localhost")) {
            log.warning("You have not defined your own server, we will not really deploy to localhost!");
            return true;
        }
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(getIP_Address());
            if (ftp.login(getUserName(), getPassword())) log.info("Connected to " + getIP_Address() + " as " + getUserName()); else {
                log.warning("Could NOT connect to " + getIP_Address() + " as " + getUserName());
                return false;
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Could NOT connect to " + getIP_Address() + " as " + getUserName(), e);
            return false;
        }
        boolean success = true;
        String cmd = null;
        try {
            cmd = "cwd";
            ftp.changeWorkingDirectory(getFolder());
            cmd = "list";
            String[] fileNames = ftp.listNames();
            log.log(Level.FINE, "Number of files in " + getFolder() + ": " + fileNames.length);
            cmd = "bin";
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            for (int i = 0; i < media.length; i++) {
                if (!media[i].isSummary()) {
                    log.log(Level.INFO, " Deploying Media Item:" + media[i].get_ID() + media[i].getExtension());
                    MImage thisImage = media[i].getImage();
                    byte[] buffer = thisImage.getData();
                    ByteArrayInputStream is = new ByteArrayInputStream(buffer);
                    String fileName = media[i].get_ID() + media[i].getExtension();
                    cmd = "put " + fileName;
                    ftp.storeFile(fileName, is);
                    is.close();
                }
            }
        } catch (Exception e) {
            log.log(Level.WARNING, cmd, e);
            success = false;
        }
        try {
            cmd = "logout";
            ftp.logout();
            cmd = "disconnect";
            ftp.disconnect();
        } catch (Exception e) {
            log.log(Level.WARNING, cmd, e);
        }
        ftp = null;
        return success;
    }

    public boolean saveVideoXMLOnWebserver(String text) {
        boolean error = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(this.getWebserver().getUrl());
            System.out.println("Connected to " + this.getWebserver().getUrl() + ".");
            System.out.print(ftp.getReplyString());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return false;
            }
            if (!ftp.login(this.getWebserver().getFtpBenutzer(), this.getWebserver().getFtpPasswort())) {
                System.err.println("FTP server: Login incorrect");
            }
            String tmpSeminarID = this.getSeminarID();
            if (tmpSeminarID == null) tmpSeminarID = "unbekannt";
            try {
                ftp.changeWorkingDirectory(this.getWebserver().getDefaultPath() + "/" + tmpSeminarID + "/lectures/" + this.getId() + "/data");
            } catch (Exception e) {
                ftp.makeDirectory(this.getWebserver().getDefaultPath() + "/" + tmpSeminarID + "/lectures/" + this.getId() + "/data");
                ftp.changeWorkingDirectory(this.getWebserver().getDefaultPath() + "/" + tmpSeminarID + "/lectures/" + this.getId() + "/data");
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ByteArrayInputStream videoIn = new ByteArrayInputStream(text.getBytes());
            ftp.enterLocalPassiveMode();
            ftp.storeFile("video.xml", videoIn);
            videoIn.close();
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            System.err.println("Job " + this.getId() + ": Datei video.xml konnte nicht auf Webserver kopiert werden.");
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return error;
    }

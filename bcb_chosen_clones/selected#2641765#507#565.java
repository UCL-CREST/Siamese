    public String loadGeneratorXML() {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(this.getFolienKonvertierungsServer().getUrl());
            System.out.println("Connected to " + this.getFolienKonvertierungsServer().getUrl() + ".");
            System.out.print(ftp.getReplyString());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return null;
            }
            if (!ftp.login(this.getFolienKonvertierungsServer().getFtpBenutzer(), this.getFolienKonvertierungsServer().getFtpPasswort())) {
                System.err.println("FTP server: Login incorrect");
            }
            String path;
            if (this.getFolienKonvertierungsServer().getDefaultPath().length() > 0) {
                path = "/" + this.getFolienKonvertierungsServer().getDefaultPath() + "/" + this.getId() + "/";
            } else {
                path = "/" + this.getId() + "/";
            }
            if (!ftp.changeWorkingDirectory(path)) System.err.println("Konnte Verzeichnis nicht wechseln: " + path);
            System.err.println("Arbeitsverzeichnis: " + ftp.printWorkingDirectory());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            InputStream inStream = ftp.retrieveFileStream("generator.xml");
            if (inStream == null) {
                System.err.println("Job " + this.getId() + ": Datei generator.xml wurde nicht gefunden");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            generatorXML = "";
            String zeile = "";
            while ((zeile = in.readLine()) != null) {
                generatorXML += zeile + "\n";
            }
            in.close();
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            System.err.println("Job " + this.getId() + ": Datei generator.xml konnte nicht vom Webserver kopiert werden.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Job " + this.getId() + ": Datei generator.xml konnte nicht vom Webserver kopiert werden.");
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        if (generatorXML != null && generatorXML.length() == 0) {
            generatorXML = null;
        }
        return generatorXML;
    }

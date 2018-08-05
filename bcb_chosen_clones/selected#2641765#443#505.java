    public boolean saveLecturerecordingsXMLOnWebserver() {
        boolean error = false;
        FTPClient ftp = new FTPClient();
        String lecture = "";
        try {
            URL url = new URL("http://localhost:8080/virtPresenterVerwalter/lecturerecordings.jsp?seminarid=" + this.getSeminarID());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String zeile = "";
            while ((zeile = in.readLine()) != null) {
                lecture += zeile + "\n";
            }
            in.close();
            http.disconnect();
        } catch (Exception e) {
            System.err.println("Konnte lecturerecordings.xml nicht lesen.");
        }
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
                ftp.changeWorkingDirectory(this.getWebserver().getDefaultPath() + "/" + tmpSeminarID + "/lectures/");
            } catch (Exception e) {
                ftp.makeDirectory(this.getWebserver().getDefaultPath() + "/" + tmpSeminarID + "/lectures/");
                ftp.changeWorkingDirectory(this.getWebserver().getDefaultPath() + "/" + tmpSeminarID + "/lectures/");
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ByteArrayInputStream lectureIn = new ByteArrayInputStream(lecture.getBytes());
            System.err.println("FTP Verzeichnis: " + ftp.printWorkingDirectory());
            ftp.storeFile("lecturerecordings.xml", lectureIn);
            lectureIn.close();
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            System.err.println("Job " + this.getId() + ": Datei lecturerecordings.xml konnte nicht auf Webserver kopiert werden.");
            error = true;
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Job " + this.getId() + ": Datei lecturerecordings.xml konnte nicht auf Webserver kopiert werden. (Kein Webserver zugewiesen)");
            error = true;
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

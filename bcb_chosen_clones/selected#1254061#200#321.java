    public void PutFile(ClientConnector cc, Map<String, String> attributes) throws Exception {
        String destinationNode = attributes.get("dest_name");
        String destinationUser = attributes.get("dest_user");
        String destinationPassword = attributes.get("dest_password");
        String destinationFile = attributes.get("dest_file");
        String messageID = attributes.get("messageID");
        String destinationFileType = attributes.get("dest_file_type");
        Integer destinationPort = 21;
        String destinationPortString = attributes.get("dest_port");
        if ((destinationPortString != null) && (destinationPortString.equals(""))) {
            try {
                destinationPort = Integer.parseInt(destinationPortString);
            } catch (Exception e) {
                destinationPort = 21;
                log.debug("Destination Port \"" + destinationPortString + "\" was not valid. Using Default (21)");
            }
        }
        log.info("Starting FTP push of \"" + destinationFile + "\" to \"" + destinationNode);
        if ((destinationUser == null) || (destinationUser.equals(""))) {
            List userDBVal = axt.db.GeneralDAO.getNodeValue(destinationNode, "ftpUser");
            if (userDBVal.size() < 1) {
                destinationUser = DEFAULTUSER;
            } else {
                destinationUser = (String) userDBVal.get(0);
            }
        }
        if ((destinationPassword == null) || (destinationPassword.equals(""))) {
            List passwordDBVal = axt.db.GeneralDAO.getNodeValue(destinationNode, "ftpPassword");
            if (passwordDBVal.size() < 1) {
                destinationPassword = DEFAULTPASSWORD;
            } else {
                destinationPassword = (String) passwordDBVal.get(0);
            }
        }
        log.debug("Getting Stage File ID");
        String stageFile = null;
        try {
            stageFile = STAGINGDIR + "/" + axt.db.GeneralDAO.getStageFile(messageID);
        } catch (Exception stageException) {
            throw new Exception("Failed to assign a staging file \"" + stageFile + "\" - ERROR: " + stageException);
        }
        InputStream in;
        try {
            in = new FileInputStream(stageFile);
        } catch (FileNotFoundException fileNFException) {
            throw new Exception("Failed to get the staging file \"" + stageFile + "\" - ERROR: " + fileNFException);
        }
        log.debug("Sending File");
        FTPClient ftp = new FTPClient();
        try {
            log.debug("Connecting");
            ftp.connect(destinationNode, destinationPort);
            log.debug("Checking Status");
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new Exception("Failed to connect to \"" + destinationNode + "\"  as user \"" + destinationUser + "\" - ERROR: " + ftp.getReplyString());
            }
            log.debug("Logging In");
            if (!ftp.login(destinationUser, destinationPassword)) {
                ftp.disconnect();
                throw new Exception("Failed to connect to \"" + destinationNode + "\"  as user \"" + destinationUser + "\" - ERROR: Login Failed");
            }
        } catch (SocketException socketException) {
            throw new Exception("Failed to connect to \"" + destinationNode + "\"  as user \"" + destinationUser + "\" - ERROR: " + socketException);
        } catch (IOException ioe) {
            throw new Exception("Failed to connect to \"" + destinationNode + "\"  as user \"" + destinationUser + "\" - ERROR: " + ioe);
        }
        log.debug("Performing Site Commands");
        Iterator siteIterator = GeneralDAO.getNodeValue(destinationNode, "ftpSite").iterator();
        while (siteIterator.hasNext()) {
            String siteCommand = null;
            try {
                siteCommand = (String) siteIterator.next();
                ftp.site(siteCommand);
            } catch (IOException e) {
                throw new Exception("FTP \"site\" command \"" + siteCommand + "\" failed - ERROR: " + e);
            }
        }
        if (destinationFileType != null) {
            if (destinationFileType.equals("A")) {
                log.debug("Set File Type to ASCII");
                ftp.setFileType(FTP.ASCII_FILE_TYPE);
            } else if (destinationFileType.equals("B")) {
                log.debug("Set File Type to BINARY");
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            } else if (destinationFileType.equals("E")) {
                log.debug("Set File Type to EBCDIC");
                ftp.setFileType(FTP.EBCDIC_FILE_TYPE);
            }
        }
        log.debug("Pushing File");
        OutputStream out = null;
        try {
            out = ftp.storeFileStream(destinationFile);
            if (out == null) {
                throw new Exception("Failed send the file \"" + destinationFile + "\" to \"" + destinationNode + "\"  - ERROR: " + ftp.getReplyString());
            }
        } catch (IOException ioe2) {
            log.error("Failed to push the file \"" + destinationFile + "\" to \"" + destinationNode + "\"  - ERROR: " + ioe2);
        }
        DESCrypt decrypter = null;
        try {
            decrypter = new DESCrypt();
        } catch (Exception cryptInitError) {
            log.error("Failed to initialize the encrypt process - ERROR: " + cryptInitError);
        }
        try {
            decrypter.decrypt(in, out);
        } catch (Exception cryptError) {
            log.error("Send Error" + cryptError);
        }
        log.debug("Logging Out");
        try {
            out.close();
            ftp.logout();
            in.close();
        } catch (IOException ioe3) {
            log.error("Failed close connection to \"" + destinationNode + "\"  - ERROR: " + ioe3);
        }
        return;
    }

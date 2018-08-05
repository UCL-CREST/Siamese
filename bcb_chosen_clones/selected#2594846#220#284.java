    public void runTask(HashMap pjobParms) throws Exception {
        FTPClient lftpClient = null;
        FileOutputStream lfosTargetFile = null;
        JBJFPluginDefinition lpluginCipher = null;
        IJBJFPluginCipher theCipher = null;
        try {
            JBJFFTPDefinition lxmlFTP = null;
            if (getFTPDefinition() != null) {
                lxmlFTP = getFTPDefinition();
                this.mstrSourceDirectory = lxmlFTP.getSourceDirectory();
                this.mstrTargetDirectory = lxmlFTP.getTargetDirectory();
                this.mstrFilename = lxmlFTP.getFilename();
                this.mstrRemoteServer = lxmlFTP.getServer();
                if (getResources().containsKey("plugin-cipher")) {
                    lpluginCipher = (JBJFPluginDefinition) getResources().get("plugin-cipher");
                }
                if (lpluginCipher != null) {
                    theCipher = getTaskPlugins().getCipherPlugin(lpluginCipher.getPluginId());
                }
                if (theCipher != null) {
                    this.mstrServerUsr = theCipher.decryptString(lxmlFTP.getUser());
                    this.mstrServerPwd = theCipher.decryptString(lxmlFTP.getPass());
                } else {
                    this.mstrServerUsr = lxmlFTP.getUser();
                    this.mstrServerPwd = lxmlFTP.getPass();
                }
            } else {
                throw new Exception("Work unit [ " + SHORT_NAME + " ] is missing an FTP Definition.  Please check" + " your JBJF Batch Definition file an make sure" + " this work unit has a <resource> element added" + " within the <task> element.");
            }
            lfosTargetFile = new FileOutputStream(mstrTargetDirectory + File.separator + mstrFilename);
            lftpClient = new FTPClient();
            lftpClient.connect(mstrRemoteServer);
            lftpClient.setFileType(lxmlFTP.getFileTransferType());
            if (!FTPReply.isPositiveCompletion(lftpClient.getReplyCode())) {
                throw new Exception("FTP server [ " + mstrRemoteServer + " ] refused connection.");
            }
            if (!lftpClient.login(mstrServerUsr, mstrServerPwd)) {
                throw new Exception("Unable to login to server [ " + mstrTargetDirectory + " ].");
            }
            if (!lftpClient.changeWorkingDirectory(mstrSourceDirectory)) {
                throw new Exception("Unable to change to remote directory [ " + mstrSourceDirectory + "]");
            }
            lftpClient.enterLocalPassiveMode();
            if (!lftpClient.retrieveFile(mstrFilename, lfosTargetFile)) {
                throw new Exception("Unable to download [ " + mstrSourceDirectory + "/" + mstrFilename + " to " + mstrTargetDirectory + File.separator + mstrFilename + " ] from server [ " + mstrRemoteServer + " ]");
            }
            lfosTargetFile.close();
            lftpClient.logout();
        } catch (Exception e) {
            throw e;
        } finally {
            if (lftpClient != null && lftpClient.isConnected()) {
                try {
                    lftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
            if (lfosTargetFile != null) {
                try {
                    lfosTargetFile.close();
                } catch (Exception e) {
                }
            }
        }
    }

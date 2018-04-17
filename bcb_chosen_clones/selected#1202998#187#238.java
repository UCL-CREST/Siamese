    public static String[] viewFilesToImport(HttpSession session) {
        FTPClient ftp = new FTPClient();
        CofaxToolsUser user = (CofaxToolsUser) session.getAttribute("user");
        String importFTPServer = (String) user.workingPubConfigElementsHash.get("IMPORTFTPSERVER") + "";
        String importFTPLogin = (String) user.workingPubConfigElementsHash.get("IMPORTFTPLOGIN") + "";
        String importFTPPassword = (String) user.workingPubConfigElementsHash.get("IMPORTFTPPASSWORD") + "";
        String importFTPFilePath = (String) user.workingPubConfigElementsHash.get("IMPORTFTPFILEPATH");
        String[] dirList = null;
        if (importFTPServer.equals("") || importFTPLogin.equals("") || importFTPPassword.equals("")) {
            return dirList;
        }
        boolean loggedIn = false;
        try {
            int reply;
            ftp.connect(importFTPServer);
            CofaxToolsUtil.log("CofaxToolsFTP viewFilesToImport connecting: " + ftp.getReplyString());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.logout();
                ftp.disconnect();
                CofaxToolsUtil.log("CofaxToolsFTP viewFilesToImport ERROR: FTP server refused connection.");
            } else {
                loggedIn = ftp.login(importFTPLogin, importFTPPassword);
                CofaxToolsUtil.log("CofaxToolsFTP viewFilesToImport Logging in: " + importFTPLogin + " " + importFTPPassword);
            }
            if (loggedIn) {
                try {
                    ftp.changeWorkingDirectory(importFTPFilePath);
                    CofaxToolsUtil.log("CofaxToolsFTP viewFilesToImport changing dir: " + importFTPFilePath);
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        CofaxToolsUtil.log("ERROR: cannot change directory");
                    }
                    FTPFile[] remoteFileList = ftp.listFiles();
                    ArrayList tmpArray = new ArrayList();
                    for (int i = 0; i < remoteFileList.length; i++) {
                        FTPFile testFile = remoteFileList[i];
                        if (testFile.getType() == FTP.ASCII_FILE_TYPE) {
                            tmpArray.add(testFile.getName());
                        }
                    }
                    dirList = (String[]) tmpArray.toArray(new String[0]);
                    ftp.logout();
                    ftp.disconnect();
                } catch (java.io.IOException e) {
                    CofaxToolsUtil.log("CofaxToolsFTP viewFilesToImport cannot read directory: " + importFTPFilePath);
                }
            }
        } catch (IOException e) {
            CofaxToolsUtil.log("CofaxToolsFTP viewFilesToImport could not connect to server: " + e);
        }
        return (dirList);
    }

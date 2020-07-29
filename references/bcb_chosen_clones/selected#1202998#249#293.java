    public static String getImportFileBody(String fileName, HttpSession session) {
        FTPClient ftp = new FTPClient();
        CofaxToolsUser user = (CofaxToolsUser) session.getAttribute("user");
        String fileTransferFolder = CofaxToolsServlet.fileTransferFolder;
        String importFTPServer = (String) user.workingPubConfigElementsHash.get("IMPORTFTPSERVER");
        String importFTPLogin = (String) user.workingPubConfigElementsHash.get("IMPORTFTPLOGIN");
        String importFTPPassword = (String) user.workingPubConfigElementsHash.get("IMPORTFTPPASSWORD");
        String importFTPFilePath = (String) user.workingPubConfigElementsHash.get("IMPORTFTPFILEPATH");
        String body = ("");
        try {
            int reply;
            ftp.connect(importFTPServer);
            CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody connecting to server " + importFTPServer);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return ("CofaxToolsFTP getImportFileBody ERROR: FTP server refused connection.");
            } else {
                ftp.login(importFTPLogin, importFTPPassword);
            }
            try {
                boolean change = ftp.changeWorkingDirectory(importFTPFilePath);
                CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody changing to directory: " + importFTPFilePath);
                CofaxToolsUtil.log("Results: " + change);
                OutputStream output;
                output = new FileOutputStream(fileTransferFolder + fileName);
                boolean retrieve = ftp.retrieveFile(fileName, output);
                CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody retrieving file: " + fileName);
                CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody results: " + change);
                output.close();
                body = CofaxToolsUtil.readFile(fileTransferFolder + fileName, true);
                CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody deleting remote file: " + importFTPFilePath + fileName);
                boolean delete = ftp.deleteFile(importFTPFilePath + fileName);
                CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody results: " + delete);
                CofaxToolsUtil.log("CofaxToolsFTP getImportFileBody disconnecting from server:" + importFTPServer);
                ftp.logout();
                ftp.disconnect();
            } catch (java.io.IOException e) {
                return ("CofaxToolsFTP getImportFileBody ERROR: cannot write file: " + fileName);
            }
        } catch (IOException e) {
            return ("CofaxToolsFTP getImportFileBody  ERROR: could not connect to server: " + e);
        }
        return (body);
    }

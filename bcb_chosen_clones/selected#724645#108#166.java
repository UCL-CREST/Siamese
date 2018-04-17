    public static Map getFile(DispatchContext dctx, Map context) {
        String localFilename = (String) context.get("localFilename");
        OutputStream localFile = null;
        try {
            localFile = new FileOutputStream(localFilename);
        } catch (IOException ioe) {
            Debug.logError(ioe, "[getFile] Problem opening local file", module);
            return ServiceUtil.returnError("ERROR: Could not open local file");
        }
        List errorList = new ArrayList();
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect((String) context.get("hostname"));
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                errorList.add("Server refused connection");
            } else {
                String username = (String) context.get("username");
                String password = (String) context.get("password");
                if (!ftp.login(username, password)) {
                    errorList.add("Login failed (" + username + ", " + password + ")");
                } else {
                    Boolean binaryTransfer = (Boolean) context.get("binaryTransfer");
                    boolean binary = (binaryTransfer == null) ? false : binaryTransfer.booleanValue();
                    if (binary) {
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    }
                    Boolean passiveMode = (Boolean) context.get("passiveMode");
                    boolean passive = (passiveMode == null) ? false : passiveMode.booleanValue();
                    if (passive) {
                        ftp.enterLocalPassiveMode();
                    }
                    if (!ftp.retrieveFile((String) context.get("remoteFilename"), localFile)) {
                        errorList.add("File not received succesfully: " + ftp.getReplyString());
                    }
                }
                ftp.logout();
            }
        } catch (IOException ioe) {
            errorList.add("Problem with FTP transfer: " + ioe.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException dce) {
                    Debug.logWarning(dce, "[getFile] Problem with FTP disconnect", module);
                }
            }
        }
        try {
            localFile.close();
        } catch (IOException ce) {
            Debug.logWarning(ce, "[getFile] Problem closing local file", module);
        }
        if (errorList.size() > 0) {
            Debug.logError("[getFile] The following error(s) (" + errorList.size() + ") occurred: " + errorList, module);
            return ServiceUtil.returnError(errorList);
        }
        return ServiceUtil.returnSuccess();
    }

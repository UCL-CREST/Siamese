    public static Map putFile(DispatchContext dctx, Map context) {
        Debug.logInfo("[putFile] starting...", module);
        InputStream localFile = null;
        try {
            localFile = new FileInputStream((String) context.get("localFilename"));
        } catch (IOException ioe) {
            Debug.logError(ioe, "[putFile] Problem opening local file", module);
            return ServiceUtil.returnError("ERROR: Could not open local file");
        }
        List errorList = new ArrayList();
        FTPClient ftp = new FTPClient();
        try {
            Debug.logInfo("[putFile] connecting to: " + (String) context.get("hostname"), module);
            ftp.connect((String) context.get("hostname"));
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                Debug.logInfo("[putFile] Server refused connection", module);
                errorList.add("connection refused");
            } else {
                String username = (String) context.get("username");
                String password = (String) context.get("password");
                Debug.logInfo("[putFile] logging in: username=" + username + ", password=" + password, module);
                if (!ftp.login(username, password)) {
                    Debug.logInfo("[putFile] login failed", module);
                    errorList.add("Login failed (" + username + ", " + password + ")");
                } else {
                    Boolean binaryTransfer = (Boolean) context.get("binaryTransfer");
                    boolean binary = (binaryTransfer == null) ? false : binaryTransfer.booleanValue();
                    if (binary) {
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    }
                    Boolean passiveMode = (Boolean) context.get("passiveMode");
                    boolean passive = (passiveMode == null) ? true : passiveMode.booleanValue();
                    if (passive) {
                        ftp.enterLocalPassiveMode();
                    }
                    Debug.logInfo("[putFile] storing local file remotely as: " + context.get("remoteFilename"), module);
                    if (!ftp.storeFile((String) context.get("remoteFilename"), localFile)) {
                        Debug.logInfo("[putFile] store was unsuccessful", module);
                        errorList.add("File not sent succesfully: " + ftp.getReplyString());
                    } else {
                        Debug.logInfo("[putFile] store was successful", module);
                        List siteCommands = (List) context.get("siteCommands");
                        if (siteCommands != null) {
                            Iterator ci = siteCommands.iterator();
                            while (ci.hasNext()) {
                                String command = (String) ci.next();
                                Debug.logInfo("[putFile] sending SITE command: " + command, module);
                                if (!ftp.sendSiteCommand(command)) {
                                    errorList.add("SITE command (" + command + ") failed: " + ftp.getReplyString());
                                }
                            }
                        }
                    }
                }
                ftp.logout();
            }
        } catch (IOException ioe) {
            Debug.log(ioe, "[putFile] caught exception: " + ioe.getMessage(), module);
            errorList.add("Problem with FTP transfer: " + ioe.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException dce) {
                    Debug.logWarning(dce, "[putFile] Problem with FTP disconnect", module);
                }
            }
        }
        try {
            localFile.close();
        } catch (IOException ce) {
            Debug.logWarning(ce, "[putFile] Problem closing local file", module);
        }
        if (errorList.size() > 0) {
            Debug.logError("[putFile] The following error(s) (" + errorList.size() + ") occurred: " + errorList, module);
            return ServiceUtil.returnError(errorList);
        }
        Debug.logInfo("[putFile] finished successfully", module);
        return ServiceUtil.returnSuccess();
    }

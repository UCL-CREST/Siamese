    public static String uploadOrDeleteMediaOrTemplates(String action, String object, String numElements, HttpServletRequest req, HttpSession session) {
        FTPClient ftp = new FTPClient();
        CofaxToolsUser user = (CofaxToolsUser) session.getAttribute("user");
        StringBuffer links = new StringBuffer();
        StringBuffer folders = new StringBuffer();
        String folder = "";
        String server = "";
        String login = "";
        String password = "";
        String fileTransferFolder = CofaxToolsServlet.fileTransferFolder;
        String liveFolder = "";
        ArrayList servers = new ArrayList();
        StringBuffer message = new StringBuffer();
        message.append("Status:<BR>");
        if (action.equals("Upload")) {
            server = (String) user.workingPubConfigElementsHash.get("TESTFTPSERVER");
            login = (String) user.workingPubConfigElementsHash.get("TESTFTPLOGIN");
            password = (String) user.workingPubConfigElementsHash.get("TESTFTPPASSWORD");
            if (object.equals("Media")) {
                folder = (String) user.workingPubConfigElementsHash.get("TESTIMAGESFOLDER");
            }
            if (object.equals("Templates")) {
                folder = (String) user.workingPubConfigElementsHash.get("TESTTEMPLATEFOLDER");
            }
        }
        if (action.equals("Delete")) {
            login = (String) user.workingPubConfigElementsHash.get("LIVEFTPLOGIN");
            password = (String) user.workingPubConfigElementsHash.get("LIVEFTPPASSWORD");
            if (object.equals("Media")) {
                server = (String) user.workingPubConfigElementsHash.get("LIVEIMAGESSERVER");
                folder = (String) user.workingPubConfigElementsHash.get("LIVEIMAGESFOLDER");
            }
            if (object.equals("Templates")) {
                server = (String) user.workingPubConfigElementsHash.get("LIVEFTPSERVER");
                folder = (String) user.workingPubConfigElementsHash.get("LIVETEMPLATEFOLDER");
            }
        }
        ArrayList al = new ArrayList();
        int numElement = Integer.parseInt(numElements);
        for (int i = 0; i < numElement; i++) {
            String key = String.valueOf(i);
            String file = req.getParameter(key);
            if (file != null) {
                al.add(file);
            }
        }
        if (action.equals("Upload")) {
            try {
                int reply;
                ftp.connect(server);
                CofaxToolsUtil.log(ftp.getReplyString());
                reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    return ("FTP server refused connection.");
                } else {
                    ftp.login(login, password);
                }
                for (int ii = 0; ii < al.size(); ii++) {
                    String fileName = (String) al.get(ii);
                    String folderName = stripName(fileName);
                    fileName = stripPath(fileName);
                    try {
                        ftp.changeWorkingDirectory(folderName);
                        OutputStream output;
                        output = new FileOutputStream(fileTransferFolder + fileName);
                        ftp.retrieveFile(fileName, output);
                        CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates retrieving file: " + ftp.getReplyString());
                        message.append("Retrieving file " + fileName + " to local disk.<BR>");
                        output.close();
                    } catch (java.io.IOException e) {
                        return ("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: cannot write file" + e);
                    }
                }
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: Could not connect to server: " + e);
                return ("Could not connect to server: " + e);
            }
            login = (String) user.workingPubConfigElementsHash.get("LIVEFTPLOGIN");
            password = (String) user.workingPubConfigElementsHash.get("LIVEFTPPASSWORD");
            if (object.equals("Media")) {
                server = (String) user.workingPubConfigElementsHash.get("LIVEIMAGESSERVER");
                liveFolder = (String) user.workingPubConfigElementsHash.get("LIVEIMAGESFOLDER");
            }
            if (object.equals("Templates")) {
                server = (String) user.workingPubConfigElementsHash.get("LIVEFTPSERVER");
                liveFolder = (String) user.workingPubConfigElementsHash.get("LIVETEMPLATEFOLDER");
            }
            servers = splitServers(server);
            for (int iii = 0; iii < servers.size(); iii++) {
                try {
                    int reply;
                    String connectServer = (String) servers.get(iii);
                    ftp.connect(connectServer);
                    CofaxToolsUtil.log(ftp.getReplyString());
                    reply = ftp.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        ftp.disconnect();
                        CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: server refused connection: " + connectServer);
                        return ("CofaxToolsFTP uploadOrDeleteMediaOrTemplates FTP server refused connection.");
                    } else {
                        ftp.login(login, password);
                    }
                    for (int ii = 0; ii < al.size(); ii++) {
                        String fileName = (String) al.get(ii);
                        CofaxToolsUtil.log("Original String " + fileName);
                        CofaxToolsUtil.log("Search for " + folder);
                        CofaxToolsUtil.log("Replace " + liveFolder);
                        String folderName = CofaxToolsUtil.replace(fileName, folder, liveFolder);
                        CofaxToolsUtil.log("Results: " + folderName);
                        folderName = stripName(folderName);
                        fileName = stripPath(fileName);
                        try {
                            InputStream io;
                            io = new FileInputStream(fileTransferFolder + fileName);
                            CofaxToolsUtil.log("Reading file : " + fileTransferFolder + fileName);
                            boolean directoryExists = ftp.changeWorkingDirectory(folderName);
                            if (directoryExists == false) {
                                CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates directory: " + folderName + " does not exist. Attempting to create.");
                                message.append("Directory: " + folderName + " does not exist. Attempting to create.<BR>");
                                boolean canCreatDir = ftp.makeDirectory(folderName);
                                CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates results: " + canCreatDir);
                                message.append("Results: " + canCreatDir + "<BR>");
                            }
                            boolean isStored = ftp.storeFile(fileName, io);
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates storing file: " + fileName + " in directory: " + folderName);
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates on server : " + connectServer);
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates results: " + isStored + " : " + ftp.getReplyString());
                            message.append("Storing file " + fileName + "<BR> to location " + folderName + "<BR> on server " + connectServer + ".<BR>");
                        } catch (java.io.IOException e) {
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates cannot upload file" + fileName + "<BR>To path: " + folderName + "<BR>On server " + connectServer);
                            return ("Cannot upload file" + fileName + "<BR>To path: " + folderName + "<BR>On server " + connectServer);
                        }
                    }
                    ftp.logout();
                    ftp.disconnect();
                    message.append("Success<BR><BR>");
                } catch (IOException e) {
                    CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates could not connect to server: " + e);
                    return ("Could not connect to server: " + e);
                }
            }
            if (object.equals("Templates")) {
                String cSServers = (String) user.workingPubConfigElementsHash.get("CACHESERVERS");
                System.out.println("getting cache servers: " + cSServers);
                ArrayList cServers = splitServers(cSServers);
                for (int iiii = 0; iiii < cServers.size(); iiii++) {
                    String thisClearCacheServer = (String) cServers.get(iiii);
                    try {
                        String connectServer = (String) cServers.get(iiii);
                        for (int iiiii = 0; iiiii < al.size(); iiiii++) {
                            String thisFilePath = (String) al.get(iiiii);
                            String folderNameFileName = CofaxToolsUtil.replace(thisFilePath, folder, liveFolder);
                            String URLToClear = CofaxToolsServlet.removeTemplateCache + folderNameFileName;
                            CofaxToolsClearCache clear = new CofaxToolsClearCache("HTTP://" + thisClearCacheServer + URLToClear);
                            clear.start();
                            message.append("Clearing Cache for " + folderNameFileName + "<BR>");
                            message.append("on server " + thisClearCacheServer + "<BR>Success<BR><BR>");
                        }
                    } catch (Exception e) {
                        CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: could not connect to server clearing cache " + e);
                    }
                }
            }
            for (int i = 0; i < al.size(); i++) {
                String fileName = (String) al.get(i);
                String folderName = stripName(fileName);
                fileName = stripPath(fileName);
                File file = new File(fileTransferFolder + fileName);
                boolean delete = file.delete();
                CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates deleting file from local drive: " + fileTransferFolder + fileName);
                CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates results: " + delete);
            }
        }
        servers = splitServers(server);
        if (action.equals("Delete")) {
            for (int iii = 0; iii < servers.size(); iii++) {
                try {
                    int reply;
                    String connectServer = (String) servers.get(iii);
                    ftp.connect(connectServer);
                    CofaxToolsUtil.log(ftp.getReplyString());
                    reply = ftp.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        ftp.disconnect();
                        CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: FTP server refused connection: " + connectServer);
                        return ("FTP server refused connection.");
                    } else {
                        ftp.login(login, password);
                    }
                    for (int ii = 0; ii < al.size(); ii++) {
                        String fileName = (String) al.get(ii);
                        String folderName = stripName(fileName);
                        fileName = stripPath(fileName);
                        try {
                            ftp.changeWorkingDirectory(folderName);
                            ftp.deleteFile(fileName);
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates deleting file: " + fileName + " from directory: " + folderName);
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates on server : " + connectServer);
                            CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates results: " + ftp.getReplyString());
                            message.append("Deleting file " + fileName + "<BR>");
                            message.append("from folder " + folderName + "<BR>");
                            message.append("on server " + connectServer + "<BR>");
                        } catch (java.io.IOException e) {
                            return ("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: cannot delete file" + fileName);
                        }
                    }
                    message.append("Success<BR><BR>");
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException e) {
                    CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR: Could not connect to server: " + e);
                    return ("Could not connect to server: " + e);
                }
            }
            if (object.equals("Templates")) {
                String cISServers = (String) user.workingPubConfigElementsHash.get("CACHESERVERS");
                ArrayList cIServers = splitServers(cISServers);
                for (int iiiiii = 0; iiiiii < cIServers.size(); iiiiii++) {
                    String thisClearCacheIServer = (String) cIServers.get(iiiiii);
                    try {
                        String connectServer = (String) cIServers.get(iiiiii);
                        for (int iiiiiii = 0; iiiiiii < al.size(); iiiiiii++) {
                            String thisFilePathI = (String) al.get(iiiiiii);
                            String URLToClearI = CofaxToolsServlet.removeTemplateCache + thisFilePathI;
                            CofaxToolsClearCache clearI = new CofaxToolsClearCache("HTTP://" + thisClearCacheIServer + URLToClearI);
                            clearI.start();
                            message.append("Clearing Cache for " + thisFilePathI + "<BR>");
                            message.append("on server " + thisClearCacheIServer + "<BR>Success<BR><BR>");
                        }
                    } catch (Exception e) {
                        CofaxToolsUtil.log("CofaxToolsFTP uploadOrDeleteMediaOrTemplates ERROR clearing cache " + e);
                    }
                }
            }
        }
        return (message.toString());
    }

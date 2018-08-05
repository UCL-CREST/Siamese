    public static String getUploadDeleteComboBox(String fromMode, String fromFolder, String action, String object, HttpSession session) {
        FTPClient ftp = new FTPClient();
        CofaxToolsUser user = (CofaxToolsUser) session.getAttribute("user");
        StringBuffer links = new StringBuffer();
        StringBuffer folders = new StringBuffer();
        String folder = "";
        String server = "";
        String login = "";
        String password = "";
        int i = 0;
        String liveFTPServer = (String) user.workingPubConfigElementsHash.get("LIVEFTPSERVER") + "";
        liveFTPServer = liveFTPServer.trim();
        if ((liveFTPServer == null) || (liveFTPServer.equals("null")) || (liveFTPServer.equals(""))) {
            return ("This tool is not " + "configured and will not operate. " + "If you wish it to do so, please edit " + "this publication's properties and add correct values to " + " the Remote Server Upstreaming section.");
        }
        if (action.equals("Upload")) {
            server = (String) user.workingPubConfigElementsHash.get("TESTFTPSERVER");
            login = (String) user.workingPubConfigElementsHash.get("TESTFTPLOGIN");
            password = (String) user.workingPubConfigElementsHash.get("TESTFTPPASSWORD");
            CofaxToolsUtil.log("server= " + server + " , login= " + login + " , password=" + password);
            if (object.equals("Media")) {
                folder = (String) user.workingPubConfigElementsHash.get("TESTIMAGESFOLDER");
            }
            if (object.equals("Templates")) {
                folder = (String) user.workingPubConfigElementsHash.get("TESTTEMPLATEFOLDER");
                CofaxToolsUtil.log("testTemplateFolder= " + folder);
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
        ArrayList servers = splitServers(server);
        try {
            int reply;
            ftp.connect((String) servers.get(0));
            CofaxToolsUtil.log("CofaxToolsFTP getUploadDeleteComboBox connecting to server: " + (String) servers.get(0));
            CofaxToolsUtil.log("CofaxToolsFTP getUploadDeleteComboBox results: " + ftp.getReplyString());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return ("CofaxToolsFTP getUploadDeleteComboBox ERROR: FTP server refused connection: " + server);
            } else {
                ftp.login(login, password);
                CofaxToolsUtil.log("CofaxToolsFTP getUploadDeleteComboBox login / pass " + login + " " + password);
            }
            try {
                String tempParentFolderName = folder;
                CofaxToolsUtil.log("fromfolder is " + fromFolder);
                if ((fromFolder != null) && (fromFolder.length() > folder.length())) {
                    folder = fromFolder;
                    tempParentFolderName = folder;
                    int subSt = folder.lastIndexOf("/");
                    tempParentFolderName = tempParentFolderName.substring(0, subSt);
                    String publicName = "";
                    int subStri = folder.lastIndexOf((String) user.workingPubName);
                    if (subStri > -1) {
                        publicName = folder.substring(subStri);
                    }
                    folders.append("<B><A HREF=\'/tools/?mode=" + fromMode + "&hl=templates_view_templates_images&fromFolder=" + tempParentFolderName + "\'>" + tempParentFolderName + "</A></B><BR>\n");
                } else if ((fromFolder != null) && (fromFolder.length() == folder.length())) {
                    folder = fromFolder;
                    tempParentFolderName = folder;
                    int subSt = folder.lastIndexOf("/");
                    tempParentFolderName = tempParentFolderName.substring(0, subSt);
                }
                boolean changed = ftp.changeWorkingDirectory(folder);
                CofaxToolsUtil.log("CofaxToolsFTP getUploadDeleteComboBox changing working directory to " + folder);
                CofaxToolsUtil.log("CofaxToolsFTP getUploadDeleteComboBox results " + changed);
                FTPFile[] files = null;
                if ((action.equals("Delete")) && (object.equals("Templates"))) {
                    files = ftp.listFiles(new CofaxToolsNTFileListParser());
                } else {
                    files = ftp.listFiles(new CofaxToolsNTFileListParser());
                }
                if (files == null) {
                    CofaxToolsUtil.log("null");
                }
                for (int ii = 0; ii < files.length; ii++) {
                    FTPFile thisFile = (FTPFile) files[ii];
                    String name = thisFile.getName();
                    if (!thisFile.isDirectory()) {
                        links.append("<INPUT TYPE=CHECKBOX NAME=" + i + " VALUE=" + folder + "/" + name + ">" + name + "<BR>\n");
                        i++;
                    }
                    if ((thisFile.isDirectory()) && (!name.startsWith(".")) && (!name.endsWith("."))) {
                        int subStr = folder.lastIndexOf((String) user.workingPubName);
                        String tempFolderName = "";
                        if (subStr > -1) {
                            tempFolderName = folder.substring(subStr);
                        } else {
                            tempFolderName = folder;
                        }
                        folders.append("<LI><A HREF=\'/tools/?mode=" + fromMode + "&hl=templates_view_templates_images&fromFolder=" + folder + "/" + name + "\'>" + tempFolderName + "/" + name + "</A><BR>");
                    }
                }
                ftp.logout();
                ftp.disconnect();
            } catch (java.io.IOException e) {
                return ("CofaxToolsFTP getUploadDeleteComboBox cannot read directory: " + folder);
            }
        } catch (IOException e) {
            return ("Could not connect to server: " + e);
        }
        links.append("<INPUT TYPE=HIDDEN NAME=numElements VALUE=" + i + " >\n");
        links.append("<INPUT TYPE=SUBMIT VALUE=\"" + action + " " + object + "\">\n");
        return (folders.toString() + links.toString());
    }

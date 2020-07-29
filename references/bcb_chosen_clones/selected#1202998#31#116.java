    public static String uploadArticleMedia(String localPath, String articleImageName, String year, String month, String day, DataStore db, HttpSession session) {
        CofaxToolsUser user = (CofaxToolsUser) session.getAttribute("user");
        if (!localPath.endsWith(File.separator)) {
            localPath += File.separator;
        }
        FTPClient ftp = new FTPClient();
        String liveFTPLogin = (String) user.workingPubConfigElementsHash.get("LIVEFTPLOGIN");
        String liveFTPPassword = (String) user.workingPubConfigElementsHash.get("LIVEFTPPASSWORD");
        String liveImagesServer = (String) user.workingPubConfigElementsHash.get("LIVEFTPSERVER");
        String liveImagesFolder = (String) user.workingPubConfigElementsHash.get("LIVEIMAGESFOLDER");
        if (!liveImagesFolder.endsWith("/")) {
            liveImagesFolder = liveImagesFolder + "/";
        }
        String liveImagesYearFolder = "";
        String liveImagesMonthFolder = "";
        String fileLocation = "";
        fileLocation += "/" + year + "/" + month + "/" + day;
        liveImagesYearFolder = liveImagesFolder + year;
        liveImagesMonthFolder = (liveImagesYearFolder + "/" + month);
        liveImagesFolder = (liveImagesMonthFolder + "/" + day);
        CofaxToolsUtil.log("CofaxToolsFTP: liveImagesServer: " + liveImagesServer);
        CofaxToolsUtil.log("CofaxToolsFTP: liveImagesFolder: " + liveImagesFolder);
        boolean stored = false;
        ArrayList servers = splitServers(liveImagesServer);
        for (int count = 0; count < servers.size(); count++) {
            String server = (String) servers.get(count);
            try {
                int reply;
                ftp.connect(server);
                CofaxToolsUtil.log("CofaxToolsFTP: uploadArticleMedia connecting to server : " + server);
                CofaxToolsUtil.log("CofaxToolsFTP: uploadArticleMedia results: " + ftp.getReplyString());
                CofaxToolsUtil.log(ftp.getReplyString());
                reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    return ("CofaxToolsFTP uploadArticleMedia ERROR: FTP server refused connection.");
                } else {
                    ftp.login(liveFTPLogin, liveFTPPassword);
                }
                try {
                    ftp.setFileType(FTP.IMAGE_FILE_TYPE);
                    InputStream input;
                    CofaxToolsUtil.log("CofaxToolsFTP: opening file stream: " + localPath + articleImageName);
                    input = new FileInputStream(localPath + articleImageName);
                    CofaxToolsUtil.log("CofaxToolsFTP: attempting to change working directory to: " + liveImagesFolder);
                    boolean changed = ftp.changeWorkingDirectory(liveImagesFolder);
                    CofaxToolsUtil.log("CofaxToolsFTP: uploadArticleMedia results: " + changed);
                    if (changed == false) {
                        CofaxToolsUtil.log("CofaxToolsFTP: uploadArticleMedia attempting to create directory :" + liveImagesFolder);
                        boolean newDirYear = ftp.makeDirectory(liveImagesYearFolder);
                        boolean newDirMonth = ftp.makeDirectory(liveImagesMonthFolder);
                        boolean newDir = ftp.makeDirectory(liveImagesFolder);
                        CofaxToolsUtil.log("CofaxToolsFTP: uploadArticleMedia results: YearDir: " + newDirYear + " MonthDir: " + newDirMonth + " finalDir: " + newDir);
                        changed = ftp.changeWorkingDirectory(liveImagesFolder);
                    }
                    if (changed) {
                        CofaxToolsUtil.log("CofaxToolsFTP: storing " + articleImageName + " to " + liveImagesFolder);
                        stored = ftp.storeFile(articleImageName, input);
                    } else {
                        CofaxToolsUtil.log("CofaxToolsFTP: failed changing: " + liveImagesFolder);
                    }
                    if (stored) {
                        CofaxToolsUtil.log("CofaxToolsFTP: Successfully ftped file.");
                    } else {
                        CofaxToolsUtil.log("CofaxToolsFTP: Failed ftping file.");
                    }
                    input.close();
                    ftp.logout();
                    ftp.disconnect();
                } catch (org.apache.commons.net.io.CopyStreamException e) {
                    CofaxToolsUtil.log("CofaxToolsFTP: Failed ftping file." + e.toString());
                    CofaxToolsUtil.log("CofaxToolsFTP: " + e.getIOException().toString());
                    return ("Cannot upload file " + liveImagesFolder + "/" + articleImageName);
                } catch (IOException e) {
                    CofaxToolsUtil.log("CofaxToolsFTP: Failed ftping file." + e.toString());
                    return ("Cannot upload file " + liveImagesFolder + "/" + articleImageName);
                } catch (Exception e) {
                    CofaxToolsUtil.log("CofaxToolsFTP: Failed ftping file." + e.toString());
                    return ("Cannot upload file " + liveImagesFolder + "/" + articleImageName);
                }
            } catch (IOException e) {
                return ("Could not connect to server: " + e);
            }
        }
        return ("");
    }

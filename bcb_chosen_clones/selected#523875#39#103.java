    public static boolean downFile(String url, String username, String password, String remotePath, Date DBLastestDate, String localPath) {
        File dFile = new File(localPath);
        if (!dFile.exists()) {
            dFile.mkdir();
        }
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setConnectTimeout(connectTimeout);
        System.out.println("FTP begin!!");
        try {
            int reply;
            ftp.connect(url);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(remotePath);
            String[] filesName = ftp.listNames();
            if (DBLastestDate == null) {
                System.out.println(" 初次下载，全部下载 ");
                for (String string : filesName) {
                    if (!string.matches("[0-9]{12}")) {
                        continue;
                    }
                    File localFile = new File(localPath + "/" + string);
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(string, is);
                    is.close();
                }
            } else {
                System.out.println(" 加一下载 ");
                Date date = DBLastestDate;
                long ldate = date.getTime();
                Date nowDate = new Date();
                String nowDateStr = Constants.DatetoString(nowDate, Constants.Time_template_LONG);
                String fileName;
                do {
                    ldate += 60 * 1000;
                    Date converterDate = new Date(ldate);
                    fileName = Constants.DatetoString(converterDate, Constants.Time_template_LONG);
                    File localFile = new File(localPath + "/" + fileName);
                    OutputStream is = new FileOutputStream(localFile);
                    if (!ftp.retrieveFile(fileName, is)) {
                        localFile.delete();
                    }
                    is.close();
                } while (fileName.compareTo(nowDateStr) < 0);
            }
            ftp.logout();
            success = true;
        } catch (IOException e) {
            System.out.println("FTP timeout return");
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

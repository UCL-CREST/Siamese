    public String FTPupload(String filepath) {
        String fileUrl = null;
        Long clicks = System.currentTimeMillis();
        String currentDateTimeString = clicks.toString();
        String[] tmpSplite = filepath.split("/");
        String filename = currentDateTimeString + tmpSplite[tmpSplite.length - 1];
        String host = "140.112.31.165:8080/sound/";
        Log.d("test", "get in");
        FTPClient ftp = new FTPClient();
        Log.d("test", "initial ftp");
        try {
            ftp.connect("140.112.31.165");
            ftp.enterLocalPassiveMode();
            Log.d("test", "we connected");
            if (!ftp.login("tacowu", "4565686")) {
                ftp.logout();
                return fileUrl;
            }
            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                Log.d("test", "get in trouble");
                ftp.disconnect();
                return fileUrl;
            }
            Log.d("test", "we logged in");
            ftp.setFileType(ftp.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            File file = new File(filepath);
            if (file == null) Log.d("test", "file open faild"); else Log.d("test", "file open sucess");
            FileInputStream aInputStream = new FileInputStream(file);
            boolean aRtn = ftp.storeFile(filename, aInputStream);
            aInputStream.close();
            ftp.disconnect();
        } catch (Exception ex) {
        }
        fileUrl = host + filename;
        return fileUrl;
    }

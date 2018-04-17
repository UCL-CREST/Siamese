    public static boolean update(String user, String pass, String channelString, String globalIP) {
        FTPClient ftp = new FTPClient();
        int reply;
        try {
            ftp.connect("witna.co.uk", 21);
            ftp.login(user, pass);
            reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                updateChannelList(ftp, channelString);
                if (!ipUpdated) {
                    ipUpdated = updateMasterChannelIP(ftp, globalIP);
                }
                ftp.disconnect();
                return true;
            } else {
                ftp.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

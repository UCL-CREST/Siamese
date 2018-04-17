    public int sftp_connect(HttpServletRequest request) {
        Map<String, Object> setting = (Map<String, Object>) request.getAttribute("globalSetting");
        int ftpssl = Common.intval(setting.get("ftpssl") + "");
        String ftphost = setting.get("ftphost") + "";
        int ftpport = Common.intval(setting.get("ftpport") + "");
        String ftpuser = setting.get("ftpuser") + "";
        String ftppassword = setting.get("ftppassword") + "";
        int ftppasv = Common.intval(setting.get("ftppasv") + "");
        String ftpdir = setting.get("ftpdir") + "";
        int ftptimeout = Common.intval(setting.get("ftptimeout") + "");
        if (ftpssl > 0) {
            try {
                fc = new FTPSClient();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return JC_FTPClientException;
            }
        } else {
            fc = new FTPClient();
        }
        try {
            fc.setConnectTimeout(20000);
            InetAddress inetAddress = InetAddress.getByName(ftphost);
            fc.connect(inetAddress, ftpport);
            if (fc.login(ftpuser, ftppassword)) {
                if (ftppasv > 0) {
                    fc.pasv();
                }
                if (ftptimeout > 0) {
                    fc.setDataTimeout(ftptimeout);
                }
                if (fc.changeWorkingDirectory(ftpdir)) {
                    return JC_FTPClientYES;
                } else {
                    FileHelper.writeLog(request, "FTP", "CHDIR " + ftpdir + " ERROR.");
                    try {
                        fc.disconnect();
                        fc = null;
                    } catch (Exception e1) {
                    }
                    return JC_FTPClientNO;
                }
            } else {
                FileHelper.writeLog(request, "FTP", "530 NOT LOGGED IN.");
                try {
                    fc.disconnect();
                    fc = null;
                } catch (Exception e1) {
                }
                return JC_FTPClientNO;
            }
        } catch (Exception e) {
            FileHelper.writeLog(request, "FTP", "COULDN'T CONNECT TO " + ftphost + ":" + ftpport + ".");
            e.printStackTrace();
            if (fc != null) {
                try {
                    fc.disconnect();
                    fc = null;
                } catch (Exception e1) {
                }
            }
            return JC_FTPClientException;
        }
    }

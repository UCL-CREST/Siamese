    public void deleteSynchrnServerFile(SynchrnServerVO synchrnServerVO) throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("euc-kr");
        if (!EgovWebUtil.isIPAddress(synchrnServerVO.getServerIp())) {
            throw new RuntimeException("IP is needed. (" + synchrnServerVO.getServerIp() + ")");
        }
        InetAddress host = InetAddress.getByName(synchrnServerVO.getServerIp());
        ftpClient.connect(host, Integer.parseInt(synchrnServerVO.getServerPort()));
        ftpClient.login(synchrnServerVO.getFtpId(), synchrnServerVO.getFtpPassword());
        FTPFile[] fTPFile = null;
        try {
            ftpClient.changeWorkingDirectory(synchrnServerVO.getSynchrnLc());
            fTPFile = ftpClient.listFiles(synchrnServerVO.getSynchrnLc());
            for (int i = 0; i < fTPFile.length; i++) {
                if (synchrnServerVO.getDeleteFileNm().equals(fTPFile[i].getName())) ftpClient.deleteFile(fTPFile[i].getName());
            }
            SynchrnServer synchrnServer = new SynchrnServer();
            synchrnServer.setServerId(synchrnServerVO.getServerId());
            synchrnServer.setReflctAt("N");
            synchrnServerDAO.processSynchrn(synchrnServer);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ftpClient.logout();
        }
    }

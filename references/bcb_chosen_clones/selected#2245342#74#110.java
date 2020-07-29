    public List<String> selectSynchrnServerFiles(SynchrnServerVO synchrnServerVO) throws Exception {
        List<String> list = new ArrayList<String>();
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.setControlEncoding("euc-kr");
            if (!EgovWebUtil.isIPAddress(synchrnServerVO.getServerIp())) {
                throw new RuntimeException("IP is needed. (" + synchrnServerVO.getServerIp() + ")");
            }
            InetAddress host = InetAddress.getByName(synchrnServerVO.getServerIp());
            try {
                ftpClient.connect(host, Integer.parseInt(synchrnServerVO.getServerPort()));
                boolean isLogin = ftpClient.login(synchrnServerVO.getFtpId(), synchrnServerVO.getFtpPassword());
                if (!isLogin) throw new Exception("FTP Client Login Error : \n");
            } catch (SocketException se) {
                System.out.println(se);
                throw new Exception(se);
            } catch (Exception e) {
                System.out.println(e);
                throw new Exception(e);
            }
            FTPFile[] fTPFile = null;
            try {
                ftpClient.changeWorkingDirectory(synchrnServerVO.getSynchrnLc());
                fTPFile = ftpClient.listFiles(synchrnServerVO.getSynchrnLc());
                for (int i = 0; i < fTPFile.length; i++) {
                    if (fTPFile[i].isFile()) list.add(fTPFile[i].getName());
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                ftpClient.logout();
            }
        } catch (Exception e) {
            list.add("noList");
        }
        return list;
    }

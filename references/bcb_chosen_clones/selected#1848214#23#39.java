    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FTPConf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        FTPConf.setServerTimeZoneId("GMT");
        FTP.configure(FTPConf);
        try {
            FTP.connect("tgftp.nws.noaa.gov");
            FTP.login("anonymous", "testing@apache.org");
            FTP.changeWorkingDirectory("SL.us008001/DF.an/DC.sflnd/DS.metar");
            FTP.enterLocalPassiveMode();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

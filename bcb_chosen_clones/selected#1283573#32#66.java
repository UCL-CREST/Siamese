    @Override
    public void run() {
        URL url = null;
        FileOutputStream fos = null;
        FTPClient ftp = null;
        try {
            url = new URL(super.getAddress());
            String host = url.getHost();
            String folder = StringUtils.substringBeforeLast(url.getPath(), "/");
            String fileName = StringUtils.substringAfterLast(url.getPath(), "/");
            ftp = new FTPClient(host, 21);
            if (!ftp.connected()) {
                ftp.connect();
            }
            ftp.login("anonymous", "me@mymail.com");
            logger.info("Connected to " + host + ".");
            logger.info(ftp.getLastValidReply().getReplyText());
            logger.debug("changing dir to " + folder);
            ftp.chdir(folder);
            fos = new FileOutputStream(localFileName);
            logger.info("Downloading file " + fileName + "...");
            ftp.setType(FTPTransferType.BINARY);
            ftp.get(fos, fileName);
            logger.info("Done.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug(e.getStackTrace());
        } finally {
            try {
                ftp.quit();
                fos.close();
            } catch (Exception e) {
            }
        }
    }

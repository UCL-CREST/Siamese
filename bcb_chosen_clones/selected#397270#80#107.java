    public void run() {
        try {
            FTPClient c = new FTPClient();
            c.configure(ftpConfig);
            logger.debug("Trying to connect");
            c.connect("127.0.0.1", 21211);
            logger.debug("Connected");
            c.setSoTimeout(5000);
            if (!FTPReply.isPositiveCompletion(c.getReplyCode())) {
                logger.debug("Houston, we have a problem. D/C");
                c.disconnect();
                throw new Exception();
            }
            if (c.login("drftpd", "drftpd")) {
                logger.debug("Logged-in, now waiting 5 secs and kill the thread.");
                _sc.addSuccess();
                Thread.sleep(5000);
                c.disconnect();
            } else {
                logger.debug("Login failed, D/C!");
                throw new Exception();
            }
        } catch (Exception e) {
            logger.debug(e, e);
            _sc.addFailure();
        }
        logger.debug("exiting");
    }

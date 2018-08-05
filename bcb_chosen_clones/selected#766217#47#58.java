    public void scan() throws Throwable {
        client = new FTPClient();
        log.info("connecting to " + host + "...");
        client.connect(host);
        log.info(client.getReplyString());
        log.info("logging in...");
        client.login("anonymous", "");
        log.info(client.getReplyString());
        Date date = Calendar.getInstance().getTime();
        xmlDocument = new XMLDocument(host, dir, date);
        scanDirectory(dir);
    }

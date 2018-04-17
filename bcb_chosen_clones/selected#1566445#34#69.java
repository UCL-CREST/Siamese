    @SuppressWarnings("static-access")
    public FastCollection<String> load(Link link) {
        URL url = null;
        FastCollection<String> links = new FastList<String>();
        FTPClient ftp = null;
        try {
            String address = link.getURI();
            address = JGetFileUtils.removeTrailingString(address, "/");
            url = new URL(address);
            host = url.getHost();
            String folder = url.getPath();
            logger.info("Traversing: " + address);
            ftp = new FTPClient(host);
            if (!ftp.connected()) {
                ftp.connect();
            }
            ftp.login("anonymous", "me@mymail.com");
            logger.info("Connected to " + host + ".");
            logger.debug("changing dir to " + folder);
            ftp.chdir(folder);
            String[] files = ftp.dir();
            for (String file : files) {
                links.add(address + "/" + file);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug(e.getStackTrace());
        } finally {
            try {
                ftp.quit();
            } catch (Exception e) {
                logger.error("Failed to logout or disconnect from the ftp server: ftp://" + host);
            }
        }
        return links;
    }

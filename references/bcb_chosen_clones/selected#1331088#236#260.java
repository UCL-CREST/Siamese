    @Override
    public String getLatestApplicationVersion() {
        String latestVersion = null;
        String latestVersionInfoURL = "http://movie-browser.googlecode.com/svn/site/latest";
        LOGGER.info("Checking latest version info from: " + latestVersionInfoURL);
        BufferedReader in = null;
        try {
            LOGGER.info("Fetcing latest version info from: " + latestVersionInfoURL);
            URL url = new URL(latestVersionInfoURL);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                latestVersion = str;
            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching latest version info from: " + latestVersionInfoURL, ex);
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                LOGGER.error("Could not close inputstream", ex);
            }
        }
        return latestVersion;
    }

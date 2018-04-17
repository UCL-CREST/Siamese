    public void run() {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Checking for updates at " + checkUrl);
            }
            URL url = new URL(checkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer content = new StringBuffer();
                String s = reader.readLine();
                while (s != null) {
                    content.append(s);
                    s = reader.readLine();
                }
                LOGGER.info("update-available", content.toString());
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No update available (Response code " + connection.getResponseCode() + ")");
            }
        } catch (Throwable e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Update check failed", e);
            }
        }
    }

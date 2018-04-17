    protected boolean registerFromFile(URI providerList) {
        boolean registeredSomething = false;
        InputStream urlStream = null;
        try {
            urlStream = providerList.toURL().openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            String provider;
            while ((provider = reader.readLine()) != null) {
                int comment = provider.indexOf('#');
                if (comment != -1) {
                    provider = provider.substring(0, comment);
                }
                provider = provider.trim();
                if (provider.length() > 0) {
                    try {
                        registeredSomething |= registerAssoc(provider);
                    } catch (Exception allElse) {
                        if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                            LOG.log(Level.WARNING, "Failed to register \'" + provider + "\'", allElse);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Failed to read provider list " + providerList, ex);
            return false;
        } finally {
            if (null != urlStream) {
                try {
                    urlStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return registeredSomething;
    }

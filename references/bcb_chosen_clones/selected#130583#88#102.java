    private static void loadDefaultSettings(final String configFileName) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(META_INF_DEFAULT_CONFIG_PROPERTIES);
            out = new FileOutputStream(configFileName);
            IOUtils.copy(in, out);
        } catch (final Exception e) {
            log.warn("Unable to pull out the default.", e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

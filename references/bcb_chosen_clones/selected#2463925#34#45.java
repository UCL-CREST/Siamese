    public static void browse(final URI uri) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(uri));
        if (null == uri) {
            throw new RuntimeExceptionIsNull("uri");
        }
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri);
        } else {
            throw new RuntimeException("Browser not supported by your machine");
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

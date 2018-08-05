    public static void open(final File file) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file));
        if (null == file) {
            throw new RuntimeExceptionIsNull("file");
        }
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        } else {
            throw new RuntimeException("Default system viewer application not supported by your machine");
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    public void actionPerformed(final ActionEvent e) {
        if (Desktop.isDesktopSupported()) {
            final String cy = RgZmSettings.getInstance().getLocale().getLanguage().toLowerCase();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Current locale: " + cy);
            }
            File docFile = new File(SAVE_TO_DIRECTORY, getIndexPage(cy) + ".html");
            if (!docFile.exists()) {
                docFile = new File(SAVE_TO_DIRECTORY, getIndexPage(DEFAULT_LOCALE) + ".html");
            }
            try {
                Desktop.getDesktop().browse(docFile.toURI());
            } catch (Throwable ex) {
                ExceptionHandler.ERRORHANDLER.handleException(ex);
            }
        }
    }

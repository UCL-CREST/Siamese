    private void desktopBrowse(String uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(uri));
            } catch (URISyntaxException urise) {
                logger.log(Level.WARNING, "Incorrect URI", urise);
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "General IO Error", ioe);
            }
        }
    }

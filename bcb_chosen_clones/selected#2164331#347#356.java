    public void help() {
        if (Desktop.isDesktopSupported()) {
            URI uri = URI.create(props.getProperty("help.url"));
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                logger.error(ex, ex);
            }
        }
    }

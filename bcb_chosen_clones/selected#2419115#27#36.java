    public void actionPerformed(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                Logging.LOG.error("Failed to open web browser", e);
            }
        }
    }

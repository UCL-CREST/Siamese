    public synchronized void openFolder(final File folder) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(folder);
        } else {
            log.warn("Desktop.isDesktopSupported()=" + Desktop.isDesktopSupported());
        }
    }

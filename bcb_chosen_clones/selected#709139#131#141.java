    private void showInBrowser(String url) {
        if (!Desktop.isDesktopSupported()) {
            log.warning("No Desktop support, can't show help from " + url);
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            log.warning("Couldn't show " + url + "; caught " + ex);
        }
    }

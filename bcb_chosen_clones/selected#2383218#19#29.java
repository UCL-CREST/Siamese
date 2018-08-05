    public boolean openUrl(URL url) throws IOException, URISyntaxException {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return false;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            return false;
        }
        desktop.browse(url.toURI());
        return true;
    }

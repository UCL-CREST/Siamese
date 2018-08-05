    private void openURI(String uriString) {
        try {
            URI uri = new URI(uriString);
            if (java.awt.Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            }
        } catch (Exception ex) {
        }
    }

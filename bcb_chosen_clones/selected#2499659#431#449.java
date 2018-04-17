    public boolean openWebsite(String url) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            logger.info("Opening websites is not supported");
            return false;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            logger.info("Opening websites is not supported");
            return false;
        }
        try {
            java.net.URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            logger.error("Unable to open website: " + e.getMessage());
            return false;
        }
        return true;
    }

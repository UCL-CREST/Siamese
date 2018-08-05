    public static void openURL(String url) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            return;
        }
        try {
            java.net.URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void openBrowserLink(String url) {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) desktop.browse(new java.net.URI(url));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

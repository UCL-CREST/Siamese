    public static boolean browse(URI uri) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
            try {
                java.awt.Desktop.getDesktop().browse(uri);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else return false;
    }

    public static boolean browse(URL url) {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Action.BROWSE)) {
            return false;
        }
        try {
            desktop.browse(url.toURI());
            return true;
        } catch (Exception exp) {
            Logs.exception(exp);
            return false;
        }
    }

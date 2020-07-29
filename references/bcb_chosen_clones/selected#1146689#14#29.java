    public static boolean browse(String url) {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Action.BROWSE)) {
            return false;
        }
        try {
            desktop.browse(new URI(url));
            return true;
        } catch (Exception exp) {
            Logs.exception(exp);
            return false;
        }
    }

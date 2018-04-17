    public static boolean open(java.io.File file) {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Action.OPEN)) {
            return false;
        }
        try {
            desktop.open(file);
            return true;
        } catch (Exception exp) {
            Logs.exception(exp);
            return false;
        }
    }

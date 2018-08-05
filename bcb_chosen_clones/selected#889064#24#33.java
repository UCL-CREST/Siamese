    public static void openUrlInBrowser(URI uri) throws IOException {
        if (Desktop.isDesktopSupported()) {
            if (desktop == null) {
                desktop = Desktop.getDesktop();
            }
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            }
        }
    }

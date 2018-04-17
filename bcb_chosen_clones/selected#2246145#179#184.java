    public static void openPath(File path) throws IOException {
        if (Desktop.isDesktopSupported()) {
            final Desktop desktop = Desktop.getDesktop();
            desktop.open(path);
        }
    }

    public static void openFile(File f) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.OPEN)) {
                d.open(f.getCanonicalFile());
            } else {
                openNative(f);
            }
        } else {
            openNative(f);
        }
    }

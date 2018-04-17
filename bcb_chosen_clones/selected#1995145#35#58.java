    public static void browseFile(File f) {
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.BROWSE)) {
                try {
                    d.browse(f.getCanonicalFile().toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    openNative(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                openNative(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

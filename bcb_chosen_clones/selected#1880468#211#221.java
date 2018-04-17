    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                ourLog.error("Failed to create hyperlink", e);
            }
        } else {
            ourLog.error("Failed to create hyperlink, no desktop available");
        }
    }

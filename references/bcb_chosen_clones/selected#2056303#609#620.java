    public static void openDesktop(final File fileToOpen) {
        if (fileToOpen != null && fileToOpen.exists()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(fileToOpen);
                }
            } catch (IOException e) {
                log.log(Level.WARNING, "Error", e);
            }
        }
    }

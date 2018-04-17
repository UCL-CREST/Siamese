    public static void open(File file) {
        if (Desktop.isDesktopSupported()) try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            log.error(e);
        }
    }

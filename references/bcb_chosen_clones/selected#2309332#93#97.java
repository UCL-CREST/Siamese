    private void open(File target) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(target.toURI());
        }
    }

    private void openURI() {
        if (uri != null) if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

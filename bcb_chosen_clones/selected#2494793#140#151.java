    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException exc) {
                feedback.registerBug("IOException occurred opening a link in the browser.", exc);
            }
        } else {
            feedback.registerBug("Desktop is not supported, cannot open browser to show link!");
        }
    }

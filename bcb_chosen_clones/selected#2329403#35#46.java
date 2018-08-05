    public void openSite(final URI uri) {
        if (Desktop.isDesktopSupported()) {
            final Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

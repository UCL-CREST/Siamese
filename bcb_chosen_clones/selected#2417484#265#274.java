    public void browse(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                logger.getLogger().log(Level.WARNING, Application.getInstance(GreenTone.class).getContext().getResourceMap().getString("ErrorMessage.cannotOpenURL") + " " + uri.getPath(), e);
            }
        }
    }

    public void open(File file) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                logger.getLogger().log(Level.WARNING, Application.getInstance(GreenTone.class).getContext().getResourceMap().getString("ErrorMessage.cannotOpenURL") + " " + file.getPath(), e);
            }
        }
    }

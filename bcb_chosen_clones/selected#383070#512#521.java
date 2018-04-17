    private static void browseHelp() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(TargetedProjectionPursuit.HELP_URL));
            } catch (Exception e) {
            }
        } else {
        }
    }

    private void doAboutWeb() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(KlangConstants.KLANGEDITOR_URL_PROJECT));
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }

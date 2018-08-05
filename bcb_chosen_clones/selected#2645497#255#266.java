    private void doAboutDonate() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(KlangConstants.KLANGEDITOR_URL_DONATE));
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }

    public void accueil() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                _frameMain._statusBar.isWork(true);
                try {
                    desktop.browse(new URI(TEXT_URL_APPLICATION));
                    _frameMain._statusBar.setStatusText(TEXT_OUVERTURE_URL_APPLICATION);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MenuAction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MenuAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                _frameMain._statusBar.isWork(false);
            }
        }
    }

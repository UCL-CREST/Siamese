    public void manuel() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                _frameMain._statusBar.isWork(true);
                try {
                    desktop.open(new File(GestureRecognition.PATH_DOC));
                    _frameMain._statusBar.setStatusText(TEXT_OUVERTURE_DOC);
                } catch (IOException ex) {
                    Logger.getLogger(MenuAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                _frameMain._statusBar.isWork(false);
            }
        }
    }

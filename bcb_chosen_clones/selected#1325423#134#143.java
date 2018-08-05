    private void siteLabelMouseClicked(java.awt.event.MouseEvent evt) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }
        try {
            NULogger.getLogger().log(Level.INFO, "{0}Opening Neembuu Site..", getClass().getName());
            Desktop.getDesktop().browse(new URI("http://neembuuuploader.sourceforge.net/"));
        } catch (Exception ex) {
        }
    }

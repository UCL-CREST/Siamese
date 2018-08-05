    private void clickLabelMouseClicked(java.awt.event.MouseEvent evt) {
        if (!Desktop.isDesktopSupported()) return;
        try {
            NULogger.getLogger().log(Level.INFO, "{0}: Link clicked.. Opening the homepage..", getClass().getName());
            Desktop.getDesktop().browse(new URI("http://neembuuuploader.sourceforge.net/"));
        } catch (Exception ex) {
            NULogger.getLogger().severe(ex.toString());
        }
    }

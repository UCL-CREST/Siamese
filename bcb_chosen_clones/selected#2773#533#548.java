    private void jMenuItemWebPageActionPerformed(java.awt.event.ActionEvent evt) {
        if (java.awt.Desktop.isDesktopSupported()) {
            URI webPage = null;
            try {
                webPage = new URI("http://www.plantstreamer.com/");
            } catch (URISyntaxException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return;
            }
            try {
                java.awt.Desktop.getDesktop().browse(webPage);
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

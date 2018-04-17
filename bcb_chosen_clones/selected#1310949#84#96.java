    private void homepageHyperlinkActionPerformed(java.awt.event.ActionEvent evt) {
        String link = homepageHyperlink.getText();
        if (Desktop.isDesktopSupported()) {
            Desktop dt = Desktop.getDesktop();
            if (dt.isSupported(Desktop.Action.BROWSE)) {
                try {
                    dt.browse(new URI(link));
                } catch (Exception ex) {
                    Logger.getLogger(GeoItMapperAboutBox.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

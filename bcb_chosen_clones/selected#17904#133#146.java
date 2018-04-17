    private void lblHomepageMouseUp(MouseEvent evt) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            return;
        }
        try {
            java.net.URI uri = new java.net.URI(this.lblHomepage.getText());
            desktop.browse(uri);
        } catch (Exception e) {
        }
    }

    public void openURI(URI uri) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            JOptionPane.showMessageDialog(this, Messages.getString("Msg_DesktopIsNotSupported"));
            return;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            JOptionPane.showMessageDialog(this, Messages.getString("Msg_BrowseActionNotSupported"));
            return;
        }
        try {
            desktop.browse(uri);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, Messages.getString("Msg_AnErrorOccuredWhileBrowsing") + " " + e.getMessage());
        }
    }

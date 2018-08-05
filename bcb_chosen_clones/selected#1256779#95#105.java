    private void showInBrowser(String url) {
        if (!Desktop.isDesktopSupported()) {
            JOptionPane.showMessageDialog(this, "No Desktop support, can't show help from " + url);
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Couldn't show " + url + "; caught " + ex);
        }
    }

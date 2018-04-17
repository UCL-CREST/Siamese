    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, UIMessages.getInstance().getMessage("online.documentation.browser.error"));
            }
        } else {
            JOptionPane.showMessageDialog(this, UIMessages.getInstance().getMessage("online.documentation.browser.error"));
        }
    }

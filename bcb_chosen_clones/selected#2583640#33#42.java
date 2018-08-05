    @Override
    public void mouseClicked(MouseEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(this.url));
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Can not open website because " + e1.getMessage(), "iGoSyncDocs", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

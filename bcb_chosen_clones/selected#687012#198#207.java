    private void launchLocalFile() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(getCurrentFile().toURI());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Could not display file.");
            }
        }
    }

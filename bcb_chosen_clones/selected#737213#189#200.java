    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to launch the link, " + "your computer is likely misconfigured.", "Cannot Launch Link", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Java is not able to launch links on your computer.", "Cannot Launch Link", JOptionPane.WARNING_MESSAGE);
        }
    }

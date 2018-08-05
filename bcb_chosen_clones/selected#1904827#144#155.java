    protected void launchHTML(String path) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(path);
            try {
                desktop.open(file);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, "Help file \"UserGuide.htm\" can't be opened.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error: Help file \"UserGuide.htm\"" + "can't be opened.");
            }
        }
    }

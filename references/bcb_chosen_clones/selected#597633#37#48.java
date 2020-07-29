    public void actionPerformed(final ActionEvent arg0) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File file = new File("UserGuide.htm");
            try {
                desktop.open(file);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, "Help file \"UserGuide.htm\" can't be opened.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error: Help file \"UserGuide.htm\"" + "can't be opened.");
            }
        }
    }

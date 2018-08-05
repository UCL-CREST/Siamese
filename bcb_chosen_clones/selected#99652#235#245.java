    @SuppressWarnings("nls")
    public void openFile(String fileName) {
        File file = new File(fileName);
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view, "Cannot open file \"" + file.getName() + "\" !", "SLOC Counter", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

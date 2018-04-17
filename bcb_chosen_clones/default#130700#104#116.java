    public void loadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        int r = chooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            try {
                String name = chooser.getSelectedFile().getAbsolutePath();
                computeDigest(loadBytes(name));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

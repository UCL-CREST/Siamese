    public void open() {
        JFileChooser fc = new JFileChooser();
        int fcReturn = fc.showOpenDialog(frame);
        if (fcReturn == JFileChooser.APPROVE_OPTION) {
            open(fc.getSelectedFile().getAbsolutePath());
        }
    }

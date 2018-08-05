    public void save() {
        JFileChooser fc = new JFileChooser();
        int fcReturn = fc.showSaveDialog(frame);
        if (fcReturn == JFileChooser.APPROVE_OPTION) {
            save(fc.getSelectedFile().getAbsolutePath());
        }
    }
